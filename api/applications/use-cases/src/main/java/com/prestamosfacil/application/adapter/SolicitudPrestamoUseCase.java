package com.prestamosfacil.application.adapter;

import com.prestamosfacil.application.port.ISolicitudPrestamoPort;
import com.prestamosfacil.application.service.ProcesadorAprobacionSolicitud;
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

@Service
public class SolicitudPrestamoUseCase implements ISolicitudPrestamoPort {

    private final ISolicitudPrestamoPersistencePort solicitudPrestamoPersistencePort;
    private final IUsuarioPersistencePort usuarioPersistencePort;
    private final ITipoPrestamoPersistencePort tipoPrestamoPersistencePort;
    private final ProcesadorAprobacionSolicitud procesadorAprobacionSolicitud;
    private final List<ValidadorSolicitud> validadores = List.of(new ValidadorTipoPrestamoActivo(),
            new ValidadorMontoDentroDeRango(), new ValidadorPlazoDentroDeRango());

    public SolicitudPrestamoUseCase(ISolicitudPrestamoPersistencePort solicitudPrestamoPersistencePort,
                                     IUsuarioPersistencePort usuarioPersistencePort,
                                     ITipoPrestamoPersistencePort tipoPrestamoPersistencePort,
                                     ProcesadorAprobacionSolicitud procesadorAprobacionSolicitud) {
        this.solicitudPrestamoPersistencePort = solicitudPrestamoPersistencePort;
        this.usuarioPersistencePort = usuarioPersistencePort;
        this.tipoPrestamoPersistencePort = tipoPrestamoPersistencePort;
        this.procesadorAprobacionSolicitud = procesadorAprobacionSolicitud;
    }

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

        return solicitudPrestamoPersistencePort.guardar(solicitud);
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
