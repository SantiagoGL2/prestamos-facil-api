package com.prestamosfacil.infrastructure.rabbitmq.publisher;

import com.prestamosfacil.infrastructure.rabbitmq.config.RabbitMQConfig;
import com.prestamosfacil.infrastructure.rabbitmq.mocks.PrestamoFacilMocks;
import com.prestamosfacil.model.NotificacionRegistroEvento;
import com.prestamosfacil.model.NotificacionSolicitudEvento;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificacionRabbitPublisherTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private NotificacionRabbitPublisher notificacionRabbitPublisher;

    @Test
    void publicarSolicitudResueltaEnviaAlExchangeYRoutingKeyCorrectos() {
        NotificacionSolicitudEvento evento = PrestamoFacilMocks.getMockNotificacionSolicitudEventoAprobado();

        notificacionRabbitPublisher.publicarSolicitudResuelta(evento);

        verify(rabbitTemplate).convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY_SOLICITUD_RESUELTA,
                evento);
    }

    @Test
    void publicarUsuarioRegistradoEnviaAlExchangeYRoutingKeyCorrectos() {
        NotificacionRegistroEvento evento = PrestamoFacilMocks.getMockNotificacionRegistroEvento();

        notificacionRabbitPublisher.publicarUsuarioRegistrado(evento);

        verify(rabbitTemplate).convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY_USUARIO_REGISTRADO,
                evento);
    }
}
