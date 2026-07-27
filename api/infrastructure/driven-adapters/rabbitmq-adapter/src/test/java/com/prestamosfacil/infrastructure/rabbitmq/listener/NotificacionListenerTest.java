package com.prestamosfacil.infrastructure.rabbitmq.listener;

import com.prestamosfacil.enums.EstadoNotificacion;
import com.prestamosfacil.infrastructure.rabbitmq.email.NotificacionEmailSender;
import com.prestamosfacil.infrastructure.rabbitmq.mocks.PrestamoFacilMocks;
import com.prestamosfacil.model.Notificacion;
import com.prestamosfacil.model.NotificacionRegistroEvento;
import com.prestamosfacil.model.NotificacionSolicitudEvento;
import com.prestamosfacil.ports.INotificacionPersistencePort;

import jakarta.mail.MessagingException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificacionListenerTest {

    @Mock
    private NotificacionEmailSender notificacionEmailSender;

    @Mock
    private INotificacionPersistencePort notificacionPersistencePort;

    @InjectMocks
    private NotificacionListener notificacionListener;

    @Test
    void recibirAprobadoRegistraNotificacionEnviada() throws MessagingException {
        NotificacionSolicitudEvento evento = PrestamoFacilMocks.getMockNotificacionSolicitudEventoAprobado();

        notificacionListener.recibir(evento);

        verify(notificacionEmailSender).enviarAprobado(evento);
        ArgumentCaptor<Notificacion> captor = ArgumentCaptor.forClass(Notificacion.class);
        verify(notificacionPersistencePort).guardar(captor.capture());
        assertEquals(EstadoNotificacion.ENVIADA, captor.getValue().estadoEnvio());
        assertEquals("EMAIL_APROBACION", captor.getValue().tipo());
    }

    @Test
    void recibirRechazadoRegistraNotificacionEnviada() throws MessagingException {
        NotificacionSolicitudEvento evento = PrestamoFacilMocks.getMockNotificacionSolicitudEventoRechazado();

        notificacionListener.recibir(evento);

        verify(notificacionEmailSender).enviarRechazado(evento);
        ArgumentCaptor<Notificacion> captor = ArgumentCaptor.forClass(Notificacion.class);
        verify(notificacionPersistencePort).guardar(captor.capture());
        assertEquals(EstadoNotificacion.ENVIADA, captor.getValue().estadoEnvio());
        assertEquals("EMAIL_RECHAZO", captor.getValue().tipo());
    }

    @Test
    void recibirConFalloDeEnvioRegistraNotificacionFallida() throws MessagingException {
        NotificacionSolicitudEvento evento = PrestamoFacilMocks.getMockNotificacionSolicitudEventoAprobado();
        doThrow(new MessagingException("SMTP caido")).when(notificacionEmailSender).enviarAprobado(evento);

        notificacionListener.recibir(evento);

        ArgumentCaptor<Notificacion> captor = ArgumentCaptor.forClass(Notificacion.class);
        verify(notificacionPersistencePort).guardar(captor.capture());
        assertEquals(EstadoNotificacion.FALLIDA, captor.getValue().estadoEnvio());
    }

    @Test
    void recibirRegistroExitosoRegistraNotificacionEnviada() throws MessagingException {
        NotificacionRegistroEvento evento = PrestamoFacilMocks.getMockNotificacionRegistroEvento();

        notificacionListener.recibirRegistro(evento);

        verify(notificacionEmailSender).enviarBienvenida(evento);
        ArgumentCaptor<Notificacion> captor = ArgumentCaptor.forClass(Notificacion.class);
        verify(notificacionPersistencePort).guardar(captor.capture());
        assertEquals(EstadoNotificacion.ENVIADA, captor.getValue().estadoEnvio());
        assertEquals("EMAIL_BIENVENIDA", captor.getValue().tipo());
    }

    @Test
    void recibirRegistroConFalloDeEnvioRegistraNotificacionFallida() throws MessagingException {
        NotificacionRegistroEvento evento = PrestamoFacilMocks.getMockNotificacionRegistroEvento();
        doThrow(new MessagingException("SMTP caido")).when(notificacionEmailSender).enviarBienvenida(evento);

        notificacionListener.recibirRegistro(evento);

        ArgumentCaptor<Notificacion> captor = ArgumentCaptor.forClass(Notificacion.class);
        verify(notificacionPersistencePort).guardar(captor.capture());
        assertEquals(EstadoNotificacion.FALLIDA, captor.getValue().estadoEnvio());
    }
}
