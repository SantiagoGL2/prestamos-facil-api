package com.prestamosfacil.infrastructure.rabbitmq.listener;

import com.prestamosfacil.infrastructure.rabbitmq.config.RabbitMQConfig;
import com.prestamosfacil.infrastructure.rabbitmq.email.NotificacionEmailSender;
import com.prestamosfacil.infrastructure.rabbitmq.pdf.GeneradorPdfReporteAprobados;
import com.prestamosfacil.model.ReporteSolicitadoEvento;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ReporteListener {

    private static final Logger log = LoggerFactory.getLogger(ReporteListener.class);

    private final NotificacionEmailSender notificacionEmailSender;
    private final GeneradorPdfReporteAprobados generadorPdfReporteAprobados;

    public ReporteListener(NotificacionEmailSender notificacionEmailSender,
                            GeneradorPdfReporteAprobados generadorPdfReporteAprobados) {
        this.notificacionEmailSender = notificacionEmailSender;
        this.generadorPdfReporteAprobados = generadorPdfReporteAprobados;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_REPORTE_ENVIO)
    public void recibir(ReporteSolicitadoEvento evento) {
        try {
            byte[] pdf = generadorPdfReporteAprobados.generar(evento.prestamos(), evento.montoTotalAprobado());
            notificacionEmailSender.enviarReportePorCorreo(evento, pdf);

            log.info("Reporte de prestamos aprobados enviado a {}", evento.destinatarioEmail());
        } catch (Exception e) {
            log.error("Error enviando el reporte de prestamos aprobados a {}", evento.destinatarioEmail(), e);
        }
    }
}
