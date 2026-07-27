package com.prestamosfacil.infrastructure.rabbitmq.email;

import com.prestamosfacil.infrastructure.rabbitmq.mocks.PrestamoFacilMocks;
import com.prestamosfacil.infrastructure.rabbitmq.pdf.GeneradorPdfPlanPagos;
import com.prestamosfacil.model.NotificacionRegistroEvento;
import com.prestamosfacil.model.NotificacionSolicitudEvento;
import com.prestamosfacil.model.ReporteSolicitadoEvento;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificacionEmailSenderTest {

    private static final String HTML_VALIDO = "<p>contenido de prueba</p>";

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private GeneradorPdfPlanPagos generadorPdfPlanPagos;

    @InjectMocks
    private NotificacionEmailSender notificacionEmailSender;

    private MimeMessage nuevoMimeMessage() {
        return new MimeMessage(Session.getInstance(new Properties()));
    }

    @Test
    void enviarAprobadoGeneraElPdfYEnviaElCorreoConAdjunto() throws MessagingException {
        NotificacionSolicitudEvento evento = PrestamoFacilMocks.getMockNotificacionSolicitudEventoAprobado();
        MimeMessage mimeMessage = nuevoMimeMessage();
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("correo-aprobado"), any())).thenReturn(HTML_VALIDO);
        when(generadorPdfPlanPagos.generar(evento.usuarioNombreCompleto(), evento.tipoPrestamoNombre(),
                evento.montoAprobado(), evento.cuotaMensual(), evento.planPagos())).thenReturn("pdf".getBytes());

        notificacionEmailSender.enviarAprobado(evento);

        verify(javaMailSender).send(mimeMessage);
        assertEquals("¡Tu préstamo fue aprobado!", mimeMessage.getSubject());
        assertEquals(evento.usuarioEmail(), mimeMessage.getAllRecipients()[0].toString());
    }

    @Test
    void enviarRechazadoNoGeneraNingunPdf() throws MessagingException {
        NotificacionSolicitudEvento evento = PrestamoFacilMocks.getMockNotificacionSolicitudEventoRechazado();
        MimeMessage mimeMessage = nuevoMimeMessage();
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("correo-rechazado"), any())).thenReturn(HTML_VALIDO);

        notificacionEmailSender.enviarRechazado(evento);

        verify(javaMailSender).send(mimeMessage);
        verify(generadorPdfPlanPagos, never()).generar(any(), any(), any(), any(), any());
        assertEquals("Resultado de tu solicitud de préstamo", mimeMessage.getSubject());
    }

    @Test
    void enviarBienvenidaEnviaElCorreoDeRegistro() throws MessagingException {
        NotificacionRegistroEvento evento = PrestamoFacilMocks.getMockNotificacionRegistroEvento();
        MimeMessage mimeMessage = nuevoMimeMessage();
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("correo-bienvenida"), any())).thenReturn(HTML_VALIDO);

        notificacionEmailSender.enviarBienvenida(evento);

        verify(javaMailSender).send(mimeMessage);
        assertEquals(evento.usuarioEmail(), mimeMessage.getAllRecipients()[0].toString());
    }

    @Test
    void enviarReportePorCorreoAdjuntaElPdfRecibido() throws MessagingException {
        ReporteSolicitadoEvento evento = PrestamoFacilMocks.getMockReporteSolicitadoEvento();
        MimeMessage mimeMessage = nuevoMimeMessage();
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("correo-reporte"), any())).thenReturn(HTML_VALIDO);

        notificacionEmailSender.enviarReportePorCorreo(evento, "pdf".getBytes());

        verify(javaMailSender).send(mimeMessage);
        assertEquals(evento.destinatarioEmail(), mimeMessage.getAllRecipients()[0].toString());
    }
}
