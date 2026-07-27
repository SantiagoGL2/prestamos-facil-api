package com.prestamosfacil.infrastructure.rabbitmq.publisher;

import com.prestamosfacil.infrastructure.rabbitmq.config.RabbitMQConfig;
import com.prestamosfacil.model.NotificacionRegistroEvento;
import com.prestamosfacil.model.NotificacionSolicitudEvento;
import com.prestamosfacil.ports.INotificacionPublisherPort;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificacionRabbitPublisher implements INotificacionPublisherPort {

    private final RabbitTemplate rabbitTemplate;

    public NotificacionRabbitPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publicarSolicitudResuelta(NotificacionSolicitudEvento evento) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY_SOLICITUD_RESUELTA, evento);
    }

    @Override
    public void publicarUsuarioRegistrado(NotificacionRegistroEvento evento) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY_USUARIO_REGISTRADO, evento);
    }
}
