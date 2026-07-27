package com.prestamosfacil.application.adapter;

import com.prestamosfacil.application.port.ISolicitudPrestamoPort;
import com.prestamosfacil.application.service.ProcesadorAprobacionSolicitud;
import com.prestamosfacil.application.strategy.Aprobar;
import com.prestamosfacil.application.strategy.DecisionEvaluacion;
import com.prestamosfacil.application.strategy.EvaluacionAutomaticaStrategy;
import com.prestamosfacil.application.strategy.EvaluacionManualStrategy;
import com.prestamosfacil.application.strategy.EvaluacionPrestamoStrategy;
import com.prestamosfacil.application.strategy.Rechazar;
import com.prestamosfacil.application.strategy.RequerirRevisionManual;
import com.prestamosfacil.application.validation.SolicitudPrestamoContexto;
import com.prestamosfacil.application.validation.ValidadorMontoDentroDeRango;
import com.prestamosfacil.application.validation.ValidadorPlazoDentroDeRango;
import com.prestamosfacil.application.validation.ValidadorSolicitud;
import com.prestamosfacil.application.validation.ValidadorTipoPrestamoActivo;
import com.prestamosfacil.enums.EstadoSolicitud;
import com.prestamosfacil.enums.RolUsuario;
import com.prestamosfacil.exception.SolicitudInvalidaException;
import com.prestamosfacil.exception.SolicitudPrestamoNoEncontradaException;
import com.prestamosfacil.exception.TipoPrestamoNoEncontradoException;
import com.prestamosfacil.exception.UsuarioNoEncontradoException;
import com.prestamosfacil.model.SolicitudPrestamo;
import com.prestamosfacil.model.TipoPrestamo;
import com.prestamosfacil.model.Usuario;
import com.prestamosfacil.model.pagination.Paginacion;
import com.prestamosfacil.model.pagination.ResultadoPaginado;
import com.prestamosfacil.ports.ISolicitudPrestamoPersistencePort;
import com.prestamosfacil.ports.ITipoPrestamoPersistencePort;
import com.prestamosfacil.ports.IUsuarioPersistencePort;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Orquesta el ciclo de vida completo de una solicitud de préstamo: registro (con evaluación
 * automática opcional), consulta paginada por estado/fecha, y resolución manual por un
 * analista. Es el único punto de la app que decide si una solicitud pasa por el flujo
 * automático o queda esperando a un humano.
 */
@Service
public class SolicitudPrestamoUseCase implements ISolicitudPrestamoPort {

    private final ISolicitudPrestamoPersistencePort solicitudPrestamoPersistencePort;
    private final IUsuarioPersistencePort usuarioPersistencePort;
    private final ITipoPrestamoPersistencePort tipoPrestamoPersistencePort;
    private final ProcesadorAprobacionSolicitud procesadorAprobacionSolicitud;
    private final EvaluacionAutomaticaStrategy evaluacionAutomaticaStrategy;
    private final EvaluacionManualStrategy evaluacionManualStrategy;
    private final List<ValidadorSolicitud> validadores = List.of(new ValidadorTipoPrestamoActivo(),
            new ValidadorMontoDentroDeRango(), new ValidadorPlazoDentroDeRango());

    public SolicitudPrestamoUseCase(ISolicitudPrestamoPersistencePort solicitudPrestamoPersistencePort,
                                     IUsuarioPersistencePort usuarioPersistencePort,
                                     ITipoPrestamoPersistencePort tipoPrestamoPersistencePort,
                                     ProcesadorAprobacionSolicitud procesadorAprobacionSolicitud,
                                     EvaluacionAutomaticaStrategy evaluacionAutomaticaStrategy,
                                     EvaluacionManualStrategy evaluacionManualStrategy) {
        this.solicitudPrestamoPersistencePort = solicitudPrestamoPersistencePort;
        this.usuarioPersistencePort = usuarioPersistencePort;
        this.tipoPrestamoPersistencePort = tipoPrestamoPersistencePort;
        this.procesadorAprobacionSolicitud = procesadorAprobacionSolicitud;
        this.evaluacionAutomaticaStrategy = evaluacionAutomaticaStrategy;
        this.evaluacionManualStrategy = evaluacionManualStrategy;
    }

    /**
     * Registra la solicitud en {@code PENDIENTE_REVISION} y, solo si el tipo de préstamo tiene
     * la validación automática habilitada ({@link TipoPrestamo#validacionAutomatica()}),
     * dispara de inmediato la evaluación contra el Stored Procedure. Si el tipo de préstamo no
     * la tiene habilitada, la solicitud se deja tal cual — no tiene sentido evaluar
     * automáticamente algo que el negocio marcó explícitamente como "siempre requiere un
     * humano".
     * <p>
     * Cuando sí se evalúa, la {@link DecisionEvaluacion} resultante se resuelve así:
     * <ul>
     *   <li>{@link Aprobar}: se delega en {@link ProcesadorAprobacionSolicitud} para generar el
     *       préstamo, el plan de pagos y la notificación — igual que si lo aprobara un analista,
     *       pero con {@code analistaId = null} porque la decisión la tomó el sistema.</li>
     *   <li>{@link Rechazar}: mismo procesador, pero sin préstamo ni plan de pagos.</li>
     *   <li>{@link RequerirRevisionManual}: no se genera nada todavía, solo se persiste el
     *       estado {@code REVISION_MANUAL} para que quede claro que el sistema ya la miró y
     *       decidió que necesita un analista (distinto de {@code PENDIENTE_REVISION}, que
     *       significa que nadie la ha mirado todavía).</li>
     * </ul>
     */
    @Override
    public SolicitudPrestamo registrarSolicitud(Long usuarioId, Long tipoPrestamoId, BigDecimal monto,
                                                 int plazoMeses) {
        Usuario usuario = usuarioPersistencePort.buscarPorId(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException(usuarioId));
        TipoPrestamo tipoPrestamo = tipoPrestamoPersistencePort.buscarPorId(tipoPrestamoId)
                .orElseThrow(() -> new TipoPrestamoNoEncontradoException(tipoPrestamoId));

        SolicitudPrestamoContexto contexto = new SolicitudPrestamoContexto(usuario, tipoPrestamo, monto, plazoMeses);
        validadores.forEach(validador -> validador.validar(contexto));

        SolicitudPrestamo solicitud = new SolicitudPrestamo(null, usuario, tipoPrestamo, monto, plazoMeses,
                EstadoSolicitud.PENDIENTE_REVISION, null, LocalDateTime.now(), null);

        SolicitudPrestamo solicitudGuardada = solicitudPrestamoPersistencePort.guardar(solicitud);

        EvaluacionPrestamoStrategy strategy = tipoPrestamo.validacionAutomatica()
                ? evaluacionAutomaticaStrategy : evaluacionManualStrategy;

        if (!tipoPrestamo.validacionAutomatica()) {
            return solicitudGuardada;
        }

        DecisionEvaluacion decision = strategy.evaluar(solicitudGuardada);

        return switch (decision) {
            case Aprobar a -> procesadorAprobacionSolicitud.procesarAprobacion(solicitudGuardada, a.montoAprobado(),
                    null);
            case Rechazar r -> procesadorAprobacionSolicitud.procesarRechazo(solicitudGuardada, null);
            case RequerirRevisionManual rm -> solicitudPrestamoPersistencePort.guardar(
                    solicitudGuardada.conEstado(EstadoSolicitud.REVISION_MANUAL));
        };
    }

    @Override
    public ResultadoPaginado<SolicitudPrestamo> listarPorEstado(EstadoSolicitud estadoOpcional,
                                                                 Paginacion paginacion) {
        return solicitudPrestamoPersistencePort.listarPorEstado(estadoOpcional, paginacion);
    }

    @Override
    public ResultadoPaginado<SolicitudPrestamo> listarPorFecha(LocalDate fechaDesdeOpcional,
                                                                LocalDate fechaHastaOpcional, Paginacion paginacion) {
        if (fechaDesdeOpcional == null) {
            throw new SolicitudInvalidaException("La fecha desde es obligatoria");
        }

        if (fechaHastaOpcional == null) {
            throw new SolicitudInvalidaException("La fecha hasta es obligatoria");
        }

        LocalDate hoy = LocalDate.now();

        if (fechaDesdeOpcional.isAfter(hoy)) {
            throw new SolicitudInvalidaException("La fecha desde no puede ser posterior a la fecha actual");
        }

        if (fechaHastaOpcional.isAfter(hoy)) {
            throw new SolicitudInvalidaException("La fecha hasta no puede ser posterior a la fecha actual");
        }

        if (fechaDesdeOpcional.isAfter(fechaHastaOpcional)) {
            throw new SolicitudInvalidaException("La fecha desde no puede ser posterior a la fecha hasta");
        }

        return solicitudPrestamoPersistencePort.listarPorFecha(fechaDesdeOpcional, fechaHastaOpcional, paginacion);
    }

    /**
     * Resuelve manualmente una solicitud. Antes de delegar en
     * {@link ProcesadorAprobacionSolicitud}, valida tres cosas en orden: (1) que el estado
     * destino solo sea {@code APROBADO} o {@code RECHAZADO} — nada más tiene sentido como
     * resolución manual; (2) que la solicitud siga en un estado resoluble
     * ({@code PENDIENTE_REVISION} o {@code REVISION_MANUAL}), para no volver a resolver algo ya
     * cerrado; y (3) que el {@code analistaId} recibido corresponda a un usuario que exista y
     * que tenga específicamente rol {@code ANALISTA} — esto último evita que un cliente (o un
     * id inventado) resuelva solicitudes solo porque conoce un id de usuario válido.
     */
    @Override
    public SolicitudPrestamo actualizarEstadoManual(Long solicitudId, EstadoSolicitud nuevoEstado, Long analistaId) {
        SolicitudPrestamo solicitud = solicitudPrestamoPersistencePort.buscarPorId(solicitudId)
                .orElseThrow(() -> new SolicitudPrestamoNoEncontradaException(solicitudId));

        if (nuevoEstado != EstadoSolicitud.APROBADO && nuevoEstado != EstadoSolicitud.RECHAZADO) {
            throw new SolicitudInvalidaException(
                    "El estado manual solo puede ser APROBADO o RECHAZADO, se recibio: " + nuevoEstado);
        }

        if (solicitud.estado() != EstadoSolicitud.PENDIENTE_REVISION
                && solicitud.estado() != EstadoSolicitud.REVISION_MANUAL) {
            throw new SolicitudInvalidaException("La solicitud ya fue resuelta");
        }

        Usuario analista = usuarioPersistencePort.buscarPorId(analistaId)
                .orElseThrow(() -> new UsuarioNoEncontradoException(analistaId));

        if (analista.rol() != RolUsuario.ANALISTA) {
            throw new SolicitudInvalidaException("El usuario indicado no tiene rol de analista");
        }

        return nuevoEstado == EstadoSolicitud.APROBADO
                ? procesadorAprobacionSolicitud.procesarAprobacion(solicitud, solicitud.monto(), analistaId)
                : procesadorAprobacionSolicitud.procesarRechazo(solicitud, analistaId);
    }
}
