package com.prestamosfacil.infrastructure.oracle.adapter;

import com.prestamosfacil.infrastructure.oracle.entity.NotificacionEntity;
import com.prestamosfacil.infrastructure.oracle.mapper.NotificacionEntityMapper;
import com.prestamosfacil.infrastructure.oracle.repository.INotificacionRepository;
import com.prestamosfacil.infrastructure.oracle.repository.ISolicitudPrestamoRepository;
import com.prestamosfacil.infrastructure.oracle.repository.IUsuarioRepository;
import com.prestamosfacil.model.Notificacion;
import com.prestamosfacil.ports.INotificacionPersistencePort;

import org.springframework.stereotype.Component;

@Component
public class NotificacionPersistenceAdapter implements INotificacionPersistencePort {

    private final INotificacionRepository notificacionRepository;
    private final IUsuarioRepository usuarioRepository;
    private final ISolicitudPrestamoRepository solicitudPrestamoRepository;
    private final NotificacionEntityMapper notificacionEntityMapper;

    public NotificacionPersistenceAdapter(INotificacionRepository notificacionRepository,
                                           IUsuarioRepository usuarioRepository,
                                           ISolicitudPrestamoRepository solicitudPrestamoRepository,
                                           NotificacionEntityMapper notificacionEntityMapper) {
        this.notificacionRepository = notificacionRepository;
        this.usuarioRepository = usuarioRepository;
        this.solicitudPrestamoRepository = solicitudPrestamoRepository;
        this.notificacionEntityMapper = notificacionEntityMapper;
    }

    @Override
    public Notificacion guardar(Notificacion notificacion) {
        NotificacionEntity entity = notificacionEntityMapper.toEntity(notificacion);
        entity.setUsuario(usuarioRepository.getReferenceById(notificacion.usuarioId()));
        if (notificacion.solicitudId() != null) {
            entity.setSolicitud(solicitudPrestamoRepository.getReferenceById(notificacion.solicitudId()));
        }

        NotificacionEntity guardada = notificacionRepository.save(entity);

        return notificacionEntityMapper.toDomain(guardada);
    }
}
