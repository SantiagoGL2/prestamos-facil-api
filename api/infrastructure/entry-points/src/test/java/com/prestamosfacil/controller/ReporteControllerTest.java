package com.prestamosfacil.controller;

import com.prestamosfacil.dto.response.ReporteMontosAprobadosResponse;
import com.prestamosfacil.service.ReporteService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReporteControllerTest {

    @Mock
    private ReporteService reporteService;

    @InjectMocks
    private ReporteController reporteController;

    @Test
    void generarReporteRetorna200ConElReporte() {
        ReporteMontosAprobadosResponse reporte = new ReporteMontosAprobadosResponse(List.of(),
                BigDecimal.valueOf(1_000_000));
        when(reporteService.generarReporte()).thenReturn(reporte);

        ResponseEntity<ReporteMontosAprobadosResponse> respuesta = reporteController.generarReporte();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(reporte, respuesta.getBody());
    }

    @Test
    void enviarPorCorreoAAnalistasRetorna202YDelegaEnElServicio() {
        ResponseEntity<Void> respuesta = reporteController.enviarPorCorreoAAnalistas();

        assertEquals(HttpStatus.ACCEPTED, respuesta.getStatusCode());
        verify(reporteService, times(1)).enviarPorCorreoAAnalistas();
    }
}
