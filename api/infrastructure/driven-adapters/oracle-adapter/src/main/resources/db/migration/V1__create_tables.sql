CREATE TABLE tipo_documento (
  id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  codigo VARCHAR2(10) NOT NULL UNIQUE,
  nombre VARCHAR2(50) NOT NULL,
  activo BOOLEAN DEFAULT TRUE NOT NULL
);

CREATE TABLE usuario (
  id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  nombres VARCHAR2(100) NOT NULL,
  apellidos VARCHAR2(100) NOT NULL,
  email VARCHAR2(150) NOT NULL UNIQUE,
  tipo_documento_id NUMBER NOT NULL REFERENCES tipo_documento(id),
  numero_documento VARCHAR2(30) NOT NULL,
  salario_base NUMBER(14,2) NOT NULL,
  password_hash VARCHAR2(255) NOT NULL,
  rol VARCHAR2(20) DEFAULT 'CLIENTE' NOT NULL,
  fecha_creacion TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
  CONSTRAINT uq_usuario_doc UNIQUE (tipo_documento_id, numero_documento),
  CONSTRAINT chk_salario CHECK (salario_base BETWEEN 0 AND 15000000)
);

CREATE TABLE tipo_prestamo (
  id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  nombre VARCHAR2(100) NOT NULL,
  tasa_interes_anual NUMBER(6,4) NOT NULL,
  validacion_automatica BOOLEAN DEFAULT FALSE NOT NULL,
  monto_min NUMBER(14,2) NOT NULL,
  monto_max NUMBER(14,2) NOT NULL,
  plazo_max_meses NUMBER(4) NOT NULL,
  activo BOOLEAN DEFAULT TRUE NOT NULL
);

CREATE TABLE solicitud_prestamo (
  id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  usuario_id NUMBER NOT NULL REFERENCES usuario(id),
  tipo_prestamo_id NUMBER NOT NULL REFERENCES tipo_prestamo(id),
  monto NUMBER(14,2) NOT NULL,
  plazo_meses NUMBER(4) NOT NULL,
  estado VARCHAR2(25) DEFAULT 'PENDIENTE_REVISION' NOT NULL,
  analista_id NUMBER REFERENCES usuario(id),
  fecha_solicitud TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
  fecha_resolucion TIMESTAMP
);

CREATE TABLE prestamo (
  id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  solicitud_id NUMBER NOT NULL UNIQUE REFERENCES solicitud_prestamo(id),
  monto_aprobado NUMBER(14,2) NOT NULL,
  tasa_interes_mensual NUMBER(8,6) NOT NULL,
  cuota_mensual NUMBER(14,2) NOT NULL,
  plazo_meses NUMBER(4) NOT NULL,
  fecha_aprobacion TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
  estado VARCHAR2(20) DEFAULT 'APROBADO' NOT NULL
);

CREATE TABLE plan_pago_cuota (
  id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  prestamo_id NUMBER NOT NULL REFERENCES prestamo(id),
  numero_cuota NUMBER(4) NOT NULL,
  cuota NUMBER(14,2) NOT NULL,
  interes NUMBER(14,2) NOT NULL,
  abono_capital NUMBER(14,2) NOT NULL,
  saldo_pendiente NUMBER(14,2) NOT NULL,
  CONSTRAINT uq_plan_cuota UNIQUE (prestamo_id, numero_cuota)
);

CREATE TABLE notificacion (
  id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  usuario_id NUMBER NOT NULL REFERENCES usuario(id),
  solicitud_id NUMBER NOT NULL REFERENCES solicitud_prestamo(id),
  tipo VARCHAR2(30) NOT NULL,
  mensaje VARCHAR2(500),
  estado_envio VARCHAR2(20) DEFAULT 'PENDIENTE' NOT NULL,
  fecha_envio TIMESTAMP
);