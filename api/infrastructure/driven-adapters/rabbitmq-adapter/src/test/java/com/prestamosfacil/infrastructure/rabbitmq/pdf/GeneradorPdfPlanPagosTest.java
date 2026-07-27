package com.prestamosfacil.infrastructure.rabbitmq.pdf;

import com.prestamosfacil.infrastructure.rabbitmq.mocks.PrestamoFacilMocks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thymeleaf.TemplateEngine;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeneradorPdfPlanPagosTest {

    private static final String HTML_VALIDO = "<!DOCTYPE html><html xmlns=\"http://www.w3.org/1999/xhtml\">"
            + "<head><title>Plan de pagos</title></head><body><p>contenido de prueba</p></body></html>";

    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    private GeneradorPdfPlanPagos generadorPdfPlanPagos;

    @Test
    void generarProduceBytesDeUnPdfValido() {
        when(templateEngine.process(eq("plan-pago"), any())).thenReturn(HTML_VALIDO);

        byte[] pdf = generadorPdfPlanPagos.generar("Juan Perez", "Libre inversion", BigDecimal.valueOf(5_000_000),
                BigDecimal.valueOf(474_853.85), List.of(PrestamoFacilMocks.getMockPlanPagoCuota()));

        assertTrue(pdf.length > 0);
        assertEquals("%PDF", new String(pdf, 0, 4, StandardCharsets.US_ASCII));
    }
}
