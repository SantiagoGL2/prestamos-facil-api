package com.prestamosfacil.infrastructure.rabbitmq.publisher;

import com.prestamosfacil.infrastructure.rabbitmq.config.RabbitMQConfig;
import com.prestamosfacil.model.ReporteSolicitadoEvento;
import com.prestamosfacil.ports.IReportePublisherPort;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ReportePublisherRabbit implements IReportePublisherPort {

    private final RabbitTemplate rabbitTemplate;

    public ReportePublisherRabbit(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publicarReporteParaEnvio(ReporteSolicitadoEvento evento) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.REPORTES_EXCHANGE, RabbitMQConfig.ROUTING_KEY_REPORTE_SOLICITADO,
                evento);
    }
}
