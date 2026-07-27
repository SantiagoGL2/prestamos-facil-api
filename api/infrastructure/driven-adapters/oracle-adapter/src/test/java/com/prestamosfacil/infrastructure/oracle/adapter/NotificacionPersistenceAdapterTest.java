package com.prestamosfacil.infrastructure.oracle.adapter;

import com.prestamosfacil.enums.EstadoNotificacion;
import com.prestamosfacil.infrastructure.oracle.entity.NotificacionEntity;
import com.prestamosfacil.infrastructure.oracle.entity.SolicitudPrestamoEntity;
import com.prestamosfacil.infrastructure.oracle.entity.UsuarioEntity;
import com.prestamosfacil.infrastructure.oracle.mapper.NotificacionEntityMapper;
import com.prestamosfacil.infrastructure.oracle.mocks.PrestamoFacilMocks;
import com.prestamosfacil.infrastructure.oracle.repository.INotificacionRepository;
import com.prestamosfacil.infrastructure.oracle.repository.ISolicitudPrestamoRepository;
import com.prestamosfacil.infrastructure.oracle.repository.IUsuarioRepository;
import com.prestamosfacil.model.Notificacion;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificacionPersistenceAdapterTest {

    @Mock
    private INotificacionRepository notificacionRepository;

    @Mock
    private IUsuarioRepository usuarioRepository;

    @Mock
    private ISolicitudPrestamoRepository solicitudPrestamoRepository;

    @Mock
    private NotificacionEntityMapper notificacionEntityMapper;

    @InjectMocks
    private NotificacionPersistenceAdapter notificacionPersistenceAdapter;

    @Test
    void guardarConSolicitudAsociadaResuelveAmbasReferencias() {
        Notificacion notificacion = PrestamoFacilMocks.getMockNotificacion();
        NotificacionEntity entitySinGuardar = PrestamoFacilMocks.getMockNotificacionEntity();
        UsuarioEntity usuarioEntity = PrestamoFacilMocks.getMockUsuarioEntity();
        SolicitudPrestamoEntity solicitudEntity = PrestamoFacilMocks.getMockSolicitudPrestamoEntity();
        NotificacionEntity entityGuardada = PrestamoFacilMocks.getMockNotificacionEntity();

        when(notificacionEntityMapper.toEntity(notificacion)).thenReturn(entitySinGuardar);
        when(usuarioRepository.getReferenceById(1L)).thenReturn(usuarioEntity);
        when(solicitudPrestamoRepository.getReferenceById(100L)).thenReturn(solicitudEntity);
        when(notificacionRepository.save(entitySinGuardar)).thenReturn(entityGuardada);
        when(notificacionEntityMapper.toDomain(entityGuardada)).thenReturn(notificacion);

        Notificacion resultado = notificacionPersistenceAdapter.guardar(notificacion);

        assertEquals(notificacion, resultado);
        assertEquals(solicitudEntity, entitySinGuardar.getSolicitud());
    }

    @Test
    void guardarSinSolicitudAsociadaNoResuelveEsaReferencia() {
        Notificacion notificacion = new Notificacion(701L, 1L, null, "EMAIL_BIENVENIDA", "Correo enviado",
                EstadoNotificacion.ENVIADA, LocalDateTime.now());
        NotificacionEntity entitySinGuardar = PrestamoFacilMocks.getMockNotificacionEntity();
        UsuarioEntity usuarioEntity = PrestamoFacilMocks.getMockUsuarioEntity();
        NotificacionEntity entityGuardada = PrestamoFacilMocks.getMockNotificacionEntity();

        when(notificacionEntityMapper.toEntity(notificacion)).thenReturn(entitySinGuardar);
        when(usuarioRepository.getReferenceById(1L)).thenReturn(usuarioEntity);
        when(notificacionRepository.save(entitySinGuardar)).thenReturn(entityGuardada);
        when(notificacionEntityMapper.toDomain(entityGuardada)).thenReturn(notificacion);

        Notificacion resultado = notificacionPersistenceAdapter.guardar(notificacion);

        assertEquals(notificacion, resultado);
        verify(solicitudPrestamoRepository, never()).getReferenceById(any());
    }
}
