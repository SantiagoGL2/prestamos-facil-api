<p align="center">
  <img src="docs/logo.png" alt="Préstamos Fácil" width="360"/>
</p>

<h1 align="center">Préstamos Fácil — API</h1>

<p align="center">
  API REST para la gestión de solicitudes de préstamo: registro de clientes y analistas,
  evaluación manual y automática (vía Stored Procedure), generación de plan de pagos,
  notificaciones asíncronas por correo y reportes consolidados.
</p>

---

## Tabla de contenido

- [Stack técnico](#stack-técnico)
- [Arquitectura](#arquitectura)
- [Patrones de diseño y decisiones](#patrones-de-diseño-y-decisiones)
- [Reglas de negocio](#reglas-de-negocio)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Requisitos previos](#requisitos-previos)
- [Cómo levantar el proyecto](#cómo-levantar-el-proyecto)
- [Variables de entorno](#variables-de-entorno)
- [Usuario de prueba](#usuario-de-prueba)
- [Endpoints](#endpoints)
- [Pruebas unitarias](#pruebas-unitarias)
- [Limitaciones conocidas](#limitaciones-conocidas)

---

## Stack técnico

| Categoría              | Tecnología                                                        |
|------------------------|--------------------------------------------------------------------|
| Lenguaje               | **Java 25**                                                        |
| Framework              | **Spring Boot 4.1.0** (Spring Framework 7)                        |
| Build                  | **Maven** (multi-módulo, sin wrapper — requiere Maven instalado)  |
| Base de datos          | **Oracle 23c Free** (`gvenzl/oracle-free:23-slim`, vía Docker)     |
| Migraciones            | **Flyway** (`flyway-database-oracle`)                              |
| Persistencia           | Spring Data JPA + Hibernate, driver `ojdbc11`                      |
| Mapeo Entity ↔ Dominio | **MapStruct 1.6.3** (generado en compilación, no ModelMapper)      |
| Boilerplate de entidades | **Lombok** (solo en las entidades JPA, no en el dominio)         |
| Seguridad              | **Spring Security + JWT propio** (`jjwt 0.12.6`, sin OAuth2/Keycloak) |
| Mensajería asíncrona   | **RabbitMQ** (imagen `rabbitmq:management`)                        |
| Correo (pruebas)       | **Mailpit** — SMTP + UI web en `http://localhost:8025`             |
| Generación de PDF      | **Thymeleaf** + **OpenHTMLtoPDF 1.0.10**                            |
| Documentación API      | **springdoc-openapi 3.0.3** (Swagger UI)                            |
| Cache                  | Spring Cache (`@Cacheable`), `ConcurrentMapCacheManager` en memoria — ver [Limitaciones](#limitaciones-conocidas) |
| Pruebas                | JUnit 5 + Mockito                                                   |
| Cálculo automático     | **Stored Procedure PL/SQL** (`sp_evaluar_prestamo_automatico`)     |

## Arquitectura

Arquitectura **hexagonal**, implementada como **5 módulos Maven** (cada capa es un módulo con su
propio `pom.xml`, heredando de un `pom.xml` raíz agregador):

```
prestamos-facil-api (pom raíz)
├── domain/models                          → dominio puro: records, enums, ports, excepciones
├── applications/use-cases                 → casos de uso, estrategias, validadores
├── infrastructure/driven-adapters/
│   ├── oracle-adapter                     → JPA, repositorios, mapeo, Stored Procedure
│   └── rabbitmq-adapter                   → colas, PDF, envío de correo
└── infrastructure/entry-points            → controllers, seguridad JWT, Swagger, main
```

`domain/models` no depende de Spring, JPA ni de ninguna librería de infraestructura — es Java
puro. Los demás módulos dependen de él (nunca al revés), cumpliendo la inversión de
dependencias propia de la arquitectura hexagonal: el dominio define los *ports* (interfaces),
y cada adaptador de infraestructura los implementa.

## Patrones de diseño y decisiones

- **Strategy**: `EvaluacionPrestamoStrategy` con dos implementaciones —
  `EvaluacionAutomaticaStrategy` (delega en el Stored Procedure) y `EvaluacionManualStrategy`
  (no-op intencional, la solicitud queda pendiente para un analista). Se selecciona según
  `tipoPrestamo.validacionAutomatica()`.
- **Sealed interfaces + pattern matching** (Java 21+): `DecisionEvaluacion` (`Aprobar`,
  `Rechazar`, `RequerirRevisionManual`) se resuelve con `switch` exhaustivo, sin `default`.
- **Validadores funcionales** en lugar de una cadena de responsabilidad clásica orientada a
  objetos: `ValidadorSolicitud` se aplica en una lista procesada secuencialmente.
- **Servicio compartido** (`ProcesadorAprobacionSolicitud`): la lógica de "aprobar" (calcular
  cuota, generar plan de pagos, notificar) es idéntica sin importar si la aprobación vino de un
  analista o del Stored Procedure — vive en un único lugar, no duplicada.
- **Dominio inmutable**: los modelos son `record` de Java. Los cambios de estado (ej.
  `SolicitudPrestamo.conEstado(...)`, `.resuelta(...)`) retornan una nueva instancia en vez de
  mutar.
- **MapStruct sobre ModelMapper**: el dominio usa records inmutables (sin setters), y
  ModelMapper no puede escribir en ellos. MapStruct genera el mapeo en compilación usando el
  constructor canónico del record, con seguridad de tipos.
- **La lógica de aprobación automática vive en PL/SQL, no en Java**: el cálculo de capacidad de
  endeudamiento, deuda actual y la decisión de aprobar/rechazar/revisar ocurre dentro del
  Stored Procedure `sp_evaluar_prestamo_automatico`. La API solo interpreta el resultado.
- **`usuarioId` y `analistaId` nunca viajan en el body**: se toman del JWT autenticado
  (`@AuthenticationPrincipal UsuarioAutenticado`). Antes de tener JWT, esos campos se recibían
  en el request, lo que permitía que un usuario actuara en nombre de otro — se corrigió al
  implementar seguridad.
- **RabbitMQ + Mailpit en el mismo proceso** (no microservicios): el objetivo de la cola es
  desacoplar el flujo síncrono de la API del trabajo lento (generar PDF, enviar correo), no
  escalar independientemente. El enunciado pide un solo repositorio/sistema, así que separar en
  otro desplegable habría sido complejidad sin beneficio real para este alcance.
- **JWT propio en vez de OAuth2/Keycloak**: autocontenido, sin infraestructura adicional
  (Authorization Server, realms, clients), suficiente para el alcance de la prueba.

## Reglas de negocio

**Cuota mensual** (amortización francesa):

```
Cuota = P × (i × (1+i)^n) / ((1+i)^n − 1)
```
donde `P` = monto, `i` = tasa mensual (`tasaAnual / 12`), `n` = plazo en meses.

**Capacidad de endeudamiento** (evaluada dentro del Stored Procedure):

```
CapacidadMaxima      = SalarioBase × 0.35
DeudaActual          = SUM(cuota_mensual) de préstamos del usuario con estado APROBADO
CapacidadDisponible  = CapacidadMaxima − DeudaActual

Si CuotaNueva ≤ CapacidadDisponible:
    Si Monto > SalarioBase × 5 → REVISION_MANUAL
    Si no                      → APROBADO
Si no → RECHAZADO
```

**Flujo de una solicitud**: `PENDIENTE_REVISION` → (evaluación automática si el tipo de
préstamo la tiene habilitada, o resolución manual de un analista) → `APROBADO` / `RECHAZADO` /
`REVISION_MANUAL` (esta última solo se resuelve luego manualmente).

## Estructura del proyecto

```
domain/models/src/main/java/com/prestamosfacil/
├── model/            Usuario, SolicitudPrestamo, Prestamo, TipoPrestamo, etc. (records)
├── enums/            EstadoSolicitud, EstadoPrestamo, RolUsuario, EstadoNotificacion
├── ports/             Interfaces (persistencia, publicación de eventos, Stored Procedure)
├── exception/        Excepciones de dominio
└── constant/         Reglas de negocio como constantes (rangos, escalas, redondeo)

applications/use-cases/src/main/java/com/prestamosfacil/application/
├── port/             Ports de entrada (driving ports)
├── adapter/          Casos de uso (implementación de los ports de entrada)
├── strategy/         DecisionEvaluacion + estrategias de evaluación
├── validation/       Validadores funcionales de la solicitud
└── service/          Lógica compartida entre casos de uso

infrastructure/driven-adapters/oracle-adapter/.../infrastructure/oracle/
├── entity/           Entidades JPA (Lombok)
├── repository/       Spring Data JPA
├── mapper/           MapStruct (Entity ↔ dominio)
├── adapter/          Implementación de los ports de persistencia
└── procedure/        Invocación del Stored Procedure (SimpleJdbcCall)

infrastructure/driven-adapters/rabbitmq-adapter/.../infrastructure/rabbitmq/
├── config/           Exchanges, colas, bindings
├── publisher/        Implementación de los ports de publicación
├── listener/         Consumers (@RabbitListener)
├── pdf/               Generación de PDF (Thymeleaf + OpenHTMLtoPDF)
└── email/            Envío de correo (JavaMailSender)

infrastructure/entry-points/src/main/java/com/prestamosfacil/
├── controller/       Endpoints REST
├── service/          Capa transaccional entre el controller y el port
├── security/         JWT (filtro, servicio, UserDetails)
├── dto/               Request/response
├── exceptionHandler/ Manejo global de errores
└── config/           OpenAPI/Swagger
```

## Requisitos previos

- **JDK 25**
- **Maven** (el proyecto no incluye wrapper `mvnw`, debe estar instalado globalmente)
- **Docker** y **Docker Compose**

## Cómo levantar el proyecto

1. Clona el repositorio.

2. Crea el archivo `environment.env` en la raíz del proyecto (junto a `docker-compose.yml`),
   con las variables listadas en la sección [Variables de entorno](#variables-de-entorno).
   > **Nota**: el archivo se llama `environment.env`, no `.env` — Docker Compose no lo detecta
   > automáticamente, hay que indicarlo explícitamente con `--env-file` como se muestra abajo.

3. Levanta la infraestructura (Oracle, RabbitMQ, Mailpit):
   ```bash
   docker compose --env-file environment.env up -d
   ```
   Espera 1-2 minutos la primera vez (Oracle tarda en inicializar). Verifica con:
   ```bash
   docker compose logs -f oracle
   ```
   hasta ver `DATABASE IS READY TO USE!`.

4. Compila el proyecto:
   ```bash
   mvn clean install
   ```

5. Arranca la aplicación:
   ```bash
   mvn spring-boot:run -pl infrastructure/entry-points
   ```
   Al arrancar, Flyway aplica las migraciones automáticamente (tablas, datos semilla de tipos
   de documento/préstamo, y un usuario analista de prueba).

6. Accede a:
   - **Swagger UI**: http://localhost:8080/swagger-ui/index.html
   - **Mailpit** (correos "enviados"): http://localhost:8025
   - **RabbitMQ Management**: http://localhost:15672

## Variables de entorno

| Variable               | Descripción                                              |
|-------------------------|-----------------------------------------------------------|
| `ORACLE_PASSWORD`       | Password del usuario `SYS` del contenedor Oracle           |
| `APP_USER`              | Usuario de aplicación creado en Oracle                     |
| `APP_USER_PASSWORD`     | Password de ese usuario                                    |
| `DB_URL`                | JDBC URL, ej. `jdbc:oracle:thin:@localhost:1521/freepdb1`  |
| `DB_USERNAME`           | Debe coincidir con `APP_USER`                               |
| `DB_PASSWORD`           | Debe coincidir con `APP_USER_PASSWORD`                       |
| `RABBITMQ_HOST`         | `localhost` en desarrollo local                              |
| `RABBITMQ_PORT`         | `5672`                                                        |
| `RABBITMQ_USERNAME`     | Usuario del broker                                           |
| `RABBITMQ_PASSWORD`     | Password del broker                                          |
| `MAIL_HOST`             | `localhost` (Mailpit no requiere autenticación)              |
| `MAIL_PORT`             | `1025`                                                        |
| `SERVER_PORT`           | Puerto de la aplicación, ej. `8080`                          |
| `JWT_SECRET`            | Clave secreta HS256 (mínimo 256 bits / 32 bytes en base64)  |
| `JWT_EXPIRACION_MS`     | Vigencia del token en milisegundos, ej. `3600000` (1 hora)   |

## Usuario de prueba

La migración `V7__insert_analista_prueba.sql` deja un usuario con rol `ANALISTA` listo para
probar sin registrar nada manualmente:

```
POST /api/v1/auth/login
{
  "email": "analista.prueba@prestamosfacil.com",
  "password": "Analista123!"
}
```

Con el token de la respuesta, ya se puede listar/resolver solicitudes, registrar otros
analistas, y consultar los reportes.

## Endpoints

| Método | Ruta                                                                 | Acceso              |
|--------|------------------------------------------------------------------------|----------------------|
| POST   | `/api/v1/auth/login`                                                  | Público              |
| POST   | `/api/v1/usuarios`                                                     | Público (registro)   |
| GET    | `/api/v1/usuarios`                                                      | ANALISTA             |
| GET    | `/api/v1/usuarios/{id}`                                                | ANALISTA             |
| GET    | `/api/v1/usuarios/tipo-documento/{id}/numero-documento/{numero}`      | ANALISTA             |
| POST   | `/api/v1/analistas`                                                    | ANALISTA             |
| GET    | `/api/v1/analistas`                                                    | ANALISTA             |
| GET    | `/api/v1/tipos-documento`                                              | Público              |
| GET    | `/api/v1/tipos-prestamo`                                               | Público              |
| GET    | `/api/v1/tipos-prestamo/{id}`                                          | Público              |
| POST   | `/api/v1/solicitudes-prestamo`                                        | CLIENTE              |
| GET    | `/api/v1/solicitudes-prestamo/por-estado`                             | ANALISTA             |
| GET    | `/api/v1/solicitudes-prestamo/por-fecha`                              | ANALISTA             |
| PATCH  | `/api/v1/solicitudes-prestamo/{id}/estado`                            | ANALISTA             |
| GET    | `/api/v1/reportes/prestamos-aprobados`                                | ANALISTA             |
| POST   | `/api/v1/reportes/prestamos-aprobados/enviar-por-correo`              | ANALISTA             |

El detalle completo de cada endpoint (parámetros, respuestas, códigos de error) está en Swagger
UI una vez la app está corriendo.

**Notificaciones automáticas por correo** (asíncronas, vía RabbitMQ, visibles en Mailpit):
- Al registrar un usuario: correo de bienvenida.
- Al aprobar una solicitud: correo con el plan de pagos en PDF adjunto.
- Al rechazar una solicitud: correo informativo.
- Al solicitar el reporte: correo a cada analista con el PDF del reporte adjunto.

## Pruebas unitarias

```bash
mvn test
```

Cobertura por capa: casos de uso y estrategias de evaluación (`applications/use-cases`),
adaptadores de persistencia (`oracle-adapter`), publishers/listeners/generación de PDF
(`rabbitmq-adapter`), y controllers/servicios/seguridad JWT (`entry-points`).

## Limitaciones conocidas

- **Cache sin Redis**: `@Cacheable` funciona (tipos de documento y de préstamo), pero con el
  `CacheManager` en memoria por defecto de Spring, no con Redis — se intentó integrar Redis
  pero presentó problemas durante el desarrollo y se dejó pendiente. El cache está desacoplado
  del código de negocio, así que agregar Redis más adelante no requeriría tocar los casos de uso.
- El registro de analistas requiere que ya exista un analista autenticado (para evitar que
  cualquiera se autoasigne ese rol); por eso se siembra uno de prueba vía Flyway.
