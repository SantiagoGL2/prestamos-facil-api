package com.prestamosfacil.infrastructure.rabbitmq.listener;

import com.prestamosfacil.infrastructure.rabbitmq.email.NotificacionEmailSender;
import com.prestamosfacil.infrastructure.rabbitmq.mocks.PrestamoFacilMocks;
import com.prestamosfacil.infrastructure.rabbitmq.pdf.GeneradorPdfReporteAprobados;
import com.prestamosfacil.model.ReporteSolicitadoEvento;

import jakarta.mail.MessagingException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReporteListenerTest {

    @Mock
    private NotificacionEmailSender notificacionEmailSender;

    @Mock
    private GeneradorPdfReporteAprobados generadorPdfReporteAprobados;

    @InjectMocks
    private ReporteListener reporteListener;

    @Test
    void recibirGeneraElPdfYEnviaElCorreo() throws MessagingException {
        ReporteSolicitadoEvento evento = PrestamoFacilMocks.getMockReporteSolicitadoEvento();
        byte[] pdf = "pdf-generado".getBytes();
        when(generadorPdfReporteAprobados.generar(evento.prestamos(), evento.montoTotalAprobado())).thenReturn(pdf);

        assertDoesNotThrow(() -> reporteListener.recibir(evento));

        verify(notificacionEmailSender).enviarReportePorCorreo(evento, pdf);
    }

    @Test
    void recibirConFalloGenerandoElPdfNoRelanzaLaExcepcion() {
        ReporteSolicitadoEvento evento = PrestamoFacilMocks.getMockReporteSolicitadoEvento();
        when(generadorPdfReporteAprobados.generar(evento.prestamos(), evento.montoTotalAprobado()))
                .thenThrow(new IllegalStateException("No se pudo generar el PDF"));

        assertDoesNotThrow(() -> reporteListener.recibir(evento));
    }

    @Test
    void recibirConFalloEnviandoElCorreoNoRelanzaLaExcepcion() throws MessagingException {
        ReporteSolicitadoEvento evento = PrestamoFacilMocks.getMockReporteSolicitadoEvento();
        byte[] pdf = "pdf-generado".getBytes();
        when(generadorPdfReporteAprobados.generar(evento.prestamos(), evento.montoTotalAprobado())).thenReturn(pdf);
        doThrow(new MessagingException("SMTP caido")).when(notificacionEmailSender)
                .enviarReportePorCorreo(evento, pdf);

        assertDoesNotThrow(() -> reporteListener.recibir(evento));
    }
}
