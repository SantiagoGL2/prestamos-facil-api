package com.prestamosfacil.infrastructure.rabbitmq.publisher;

import com.prestamosfacil.infrastructure.rabbitmq.config.RabbitMQConfig;
import com.prestamosfacil.infrastructure.rabbitmq.mocks.PrestamoFacilMocks;
import com.prestamosfacil.model.ReporteSolicitadoEvento;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReportePublisherRabbitTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private ReportePublisherRabbit reportePublisherRabbit;

    @Test
    void publicarReporteParaEnvioEnviaAlExchangeYRoutingKeyCorrectos() {
        ReporteSolicitadoEvento evento = PrestamoFacilMocks.getMockReporteSolicitadoEvento();

        reportePublisherRabbit.publicarReporteParaEnvio(evento);

        verify(rabbitTemplate).convertAndSend(RabbitMQConfig.REPORTES_EXCHANGE,
                RabbitMQConfig.ROUTING_KEY_REPORTE_SOLICITADO, evento);
    }
}
