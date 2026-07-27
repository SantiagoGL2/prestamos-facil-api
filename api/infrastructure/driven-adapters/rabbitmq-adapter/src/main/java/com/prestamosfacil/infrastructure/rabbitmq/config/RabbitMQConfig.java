package com.prestamosfacil.infrastructure.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "notificaciones.exchange";

    public static final String QUEUE_SOLICITUD_RESUELTA = "notificaciones.solicitud-resuelta.queue";
    public static final String ROUTING_KEY_SOLICITUD_RESUELTA = "solicitud.resuelta";

    public static final String QUEUE_USUARIO_REGISTRADO = "notificaciones.usuario-registrado.queue";
    public static final String ROUTING_KEY_USUARIO_REGISTRADO = "usuario.registrado";

    @Bean
    public TopicExchange notificacionesExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue solicitudResueltaQueue() {
        return new Queue(QUEUE_SOLICITUD_RESUELTA);
    }

    @Bean
    public Binding solicitudResueltaBinding(Queue solicitudResueltaQueue, TopicExchange notificacionesExchange) {
        return BindingBuilder.bind(solicitudResueltaQueue).to(notificacionesExchange)
                .with(ROUTING_KEY_SOLICITUD_RESUELTA);
    }

    @Bean
    public Queue usuarioRegistradoQueue() {
        return new Queue(QUEUE_USUARIO_REGISTRADO);
    }

    @Bean
    public Binding usuarioRegistradoBinding(Queue usuarioRegistradoQueue, TopicExchange notificacionesExchange) {
        return BindingBuilder.bind(usuarioRegistradoQueue).to(notificacionesExchange)
                .with(ROUTING_KEY_USUARIO_REGISTRADO);
    }

    @Bean
    public JacksonJsonMessageConverter jacksonJsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
