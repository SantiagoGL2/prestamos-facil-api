package com.prestamosfacil.application.adapter;

import com.prestamosfacil.application.port.ISolicitudPrestamoPort;
import com.prestamosfacil.application.validation.SolicitudPrestamoContexto;
import com.prestamosfacil.application.validation.ValidadorMontoDentroDeRango;
import com.prestamosfacil.application.validation.ValidadorPlazoDentroDeRango;
import com.prestamosfacil.application.validation.ValidadorSolicitud;
import com.prestamosfacil.application.validation.ValidadorTipoPrestamoActivo;
import com.prestamosfacil.enums.EstadoSolicitud;
import com.prestamosfacil.exception.TipoPrestamoNoEncontradoException;
import com.prestamosfacil.exception.UsuarioNoEncontradoException;
import com.prestamosfacil.model.SolicitudPrestamo;
import com.prestamosfacil.model.TipoPrestamo;
import com.prestamosfacil.model.Usuario;
import com.prestamosfacil.ports.ISolicitudPrestamoPersistencePort;
import com.prestamosfacil.ports.ITipoPrestamoPersistencePort;
import com.prestamosfacil.ports.IUsuarioPersistencePort;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SolicitudPrestamoUseCase implements ISolicitudPrestamoPort {

    private final ISolicitudPrestamoPersistencePort solicitudPrestamoPersistencePort;
    private final IUsuarioPersistencePort usuarioPersistencePort;
    private final ITipoPrestamoPersistencePort tipoPrestamoPersistencePort;
    private final List<ValidadorSolicitud> validadores = List.of(new ValidadorTipoPrestamoActivo(),
            new ValidadorMontoDentroDeRango(), new ValidadorPlazoDentroDeRango());

    public SolicitudPrestamoUseCase(ISolicitudPrestamoPersistencePort solicitudPrestamoPersistencePort,
                                     IUsuarioPersistencePort usuarioPersistencePort,
                                     ITipoPrestamoPersistencePort tipoPrestamoPersistencePort) {
        this.solicitudPrestamoPersistencePort = solicitudPrestamoPersistencePort;
        this.usuarioPersistencePort = usuarioPersistencePort;
        this.tipoPrestamoPersistencePort = tipoPrestamoPersistencePort;
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
}
