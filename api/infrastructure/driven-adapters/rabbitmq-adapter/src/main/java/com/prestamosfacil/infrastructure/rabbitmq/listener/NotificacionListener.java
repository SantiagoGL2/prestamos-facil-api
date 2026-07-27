package com.prestamosfacil.infrastructure.rabbitmq.listener;

import com.prestamosfacil.enums.EstadoNotificacion;
import com.prestamosfacil.infrastructure.rabbitmq.config.RabbitMQConfig;
import com.prestamosfacil.infrastructure.rabbitmq.email.NotificacionEmailSender;
import com.prestamosfacil.model.Notificacion;
import com.prestamosfacil.model.NotificacionRegistroEvento;
import com.prestamosfacil.model.NotificacionSolicitudEvento;
import com.prestamosfacil.ports.INotificacionPersistencePort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NotificacionListener {

    private static final Logger log = LoggerFactory.getLogger(NotificacionListener.class);

    private final NotificacionEmailSender notificacionEmailSender;
    private final INotificacionPersistencePort notificacionPersistencePort;

    public NotificacionListener(NotificacionEmailSender notificacionEmailSender,
                                 INotificacionPersistencePort notificacionPersistencePort) {
        this.notificacionEmailSender = notificacionEmailSender;
        this.notificacionPersistencePort = notificacionPersistencePort;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_SOLICITUD_RESUELTA)
    public void recibir(NotificacionSolicitudEvento evento) {
        boolean aprobado = "APROBADO".equals(evento.estadoResultante());
        String tipo = aprobado ? "EMAIL_APROBACION" : "EMAIL_RECHAZO";
        EstadoNotificacion estadoEnvio;
        String mensaje;

        try {
            if (aprobado) {
                notificacionEmailSender.enviarAprobado(evento);
            } else {
                notificacionEmailSender.enviarRechazado(evento);
            }

            estadoEnvio = EstadoNotificacion.ENVIADA;
            mensaje = "Correo de " + (aprobado ? "aprobacion" : "rechazo") + " enviado a " + evento.usuarioEmail();
        } catch (Exception e) {
            log.error("Error enviando notificacion de solicitud resuelta id={}", evento.solicitudId(), e);
            estadoEnvio = EstadoNotificacion.FALLIDA;
            mensaje = "Fallo el envio del correo de " + (aprobado ? "aprobacion" : "rechazo") + ": "
                    + e.getMessage();
        }

        notificacionPersistencePort.guardar(new Notificacion(null, evento.usuarioId(), evento.solicitudId(), tipo,
                mensaje, estadoEnvio, LocalDateTime.now()));
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_USUARIO_REGISTRADO)
    public void recibirRegistro(NotificacionRegistroEvento evento) {
        EstadoNotificacion estadoEnvio;
        String mensaje;

        try {
            notificacionEmailSender.enviarBienvenida(evento);

            estadoEnvio = EstadoNotificacion.ENVIADA;
            mensaje = "Correo de bienvenida enviado a " + evento.usuarioEmail();
        } catch (Exception e) {
            log.error("Error enviando notificacion de registro de usuario id={}", evento.usuarioId(), e);
            estadoEnvio = EstadoNotificacion.FALLIDA;
            mensaje = "Fallo el envio del correo de bienvenida: " + e.getMessage();
        }

        notificacionPersistencePort.guardar(new Notificacion(null, evento.usuarioId(), null, "EMAIL_BIENVENIDA",
                mensaje, estadoEnvio, LocalDateTime.now()));
    }
}
