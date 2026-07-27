package com.prestamosfacil.application.adapter;

import com.prestamosfacil.application.port.IUsuarioPort;
import com.prestamosfacil.constant.ConfiguracionSeguridadConstantes;
import com.prestamosfacil.constant.ReglasNegocioConstantes;
import com.prestamosfacil.enums.RolUsuario;
import com.prestamosfacil.exception.DocumentoDuplicadoException;
import com.prestamosfacil.exception.EmailDuplicadoException;
import com.prestamosfacil.exception.SolicitudInvalidaException;
import com.prestamosfacil.exception.TipoDocumentoNoEncontradoException;
import com.prestamosfacil.exception.UsuarioNoEncontradoException;
import com.prestamosfacil.model.NotificacionRegistroEvento;
import com.prestamosfacil.model.TipoDocumento;
import com.prestamosfacil.model.Usuario;
import com.prestamosfacil.ports.INotificacionPublisherPort;
import com.prestamosfacil.ports.ITipoDocumentoPersistencePort;
import com.prestamosfacil.ports.IUsuarioPersistencePort;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class UsuarioUseCase implements IUsuarioPort {

    private final IUsuarioPersistencePort usuarioPersistencePort;
    private final ITipoDocumentoPersistencePort tipoDocumentoPersistencePort;
    private final INotificacionPublisherPort notificacionPublisherPort;
    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder(ConfiguracionSeguridadConstantes.FUERZA_HASH_PASSWORD);

    public UsuarioUseCase(IUsuarioPersistencePort usuarioPersistencePort,
                           ITipoDocumentoPersistencePort tipoDocumentoPersistencePort,
                           INotificacionPublisherPort notificacionPublisherPort) {
        this.usuarioPersistencePort = usuarioPersistencePort;
        this.tipoDocumentoPersistencePort = tipoDocumentoPersistencePort;
        this.notificacionPublisherPort = notificacionPublisherPort;
    }

    @Override
    public Usuario registrarUsuario(String nombres, String apellidos, String email, Long tipoDocumentoId,
                                     String numeroDocumento, BigDecimal salarioBase, String passwordPlano) {
        return registrarConRol(nombres, apellidos, email, tipoDocumentoId, numeroDocumento, salarioBase,
                passwordPlano, RolUsuario.CLIENTE);
    }

    @Override
    public Usuario registrarAnalista(String nombres, String apellidos, String email, Long tipoDocumentoId,
                                      String numeroDocumento, BigDecimal salarioBase, String passwordPlano) {
        return registrarConRol(nombres, apellidos, email, tipoDocumentoId, numeroDocumento, salarioBase,
                passwordPlano, RolUsuario.ANALISTA);
    }

    private Usuario registrarConRol(String nombres, String apellidos, String email, Long tipoDocumentoId,
                                     String numeroDocumento, BigDecimal salarioBase, String passwordPlano,
                                     RolUsuario rol) {
        TipoDocumento tipoDocumento = tipoDocumentoPersistencePort.buscarPorId(tipoDocumentoId)
                .orElseThrow(() -> new TipoDocumentoNoEncontradoException(tipoDocumentoId));

        if (usuarioPersistencePort.existeTipoDocumentoYNumeroDocumento(tipoDocumentoId, numeroDocumento)) {
            throw new DocumentoDuplicadoException(tipoDocumento.codigo(), numeroDocumento);
        }

        if (usuarioPersistencePort.existeEmail(email)) {
            throw new EmailDuplicadoException(email);
        }

        if (salarioBase.compareTo(ReglasNegocioConstantes.SALARIO_MINIMO) < 0
                || salarioBase.compareTo(ReglasNegocioConstantes.SALARIO_MAXIMO) > 0) {
            throw new SolicitudInvalidaException("El salario base debe estar entre "
                    + ReglasNegocioConstantes.SALARIO_MINIMO + " y " + ReglasNegocioConstantes.SALARIO_MAXIMO);
        }

        Usuario usuario = new Usuario(null, nombres, apellidos, email, tipoDocumento, numeroDocumento, salarioBase,
                passwordEncoder.encode(passwordPlano), rol, LocalDateTime.now());

        Usuario usuarioGuardado = usuarioPersistencePort.guardar(usuario);

        notificacionPublisherPort.publicarUsuarioRegistrado(new NotificacionRegistroEvento(usuarioGuardado.id(),
                usuarioGuardado.nombres() + " " + usuarioGuardado.apellidos(), usuarioGuardado.email(),
                usuarioGuardado.rol().name()));

        return usuarioGuardado;
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return usuarioPersistencePort.buscarPorId(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));
    }

    @Override
    public Usuario buscarPorTipoDocumentoYNumeroDocumento(Long tipoDocumentoId, String numeroDocumento) {
        return usuarioPersistencePort.buscarPorTipoDocumentoYNumeroDocumento(tipoDocumentoId, numeroDocumento)
                .orElseThrow(() -> new UsuarioNoEncontradoException(tipoDocumentoId, numeroDocumento));
    }
}
