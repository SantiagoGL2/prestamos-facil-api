package com.prestamosfacil.service;

import com.prestamosfacil.application.port.IReportePort;
import com.prestamosfacil.dto.response.ReporteMontosAprobadosResponse;
import com.prestamosfacil.model.ReporteMontosAprobados;
import com.prestamosfacil.model.ReportePrestamoAprobado;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReporteServiceTest {

    @Mock
    private IReportePort reportePort;

    @InjectMocks
    private ReporteService reporteService;

    @Test
    void generarReporteMapeaElResultadoDelPort() {
        ReporteMontosAprobados reporte = new ReporteMontosAprobados(
                List.of(new ReportePrestamoAprobado("Libre inversion", LocalDateTime.now(), 12,
                        BigDecimal.valueOf(1_000_000))),
                BigDecimal.valueOf(1_000_000));
        when(reportePort.generarReporteMontosAprobados()).thenReturn(reporte);

        ReporteMontosAprobadosResponse resultado = reporteService.generarReporte();

        assertEquals(1, resultado.prestamos().size());
        assertEquals(0, resultado.montoTotalAprobado().compareTo(BigDecimal.valueOf(1_000_000)));
    }

    @Test
    void enviarPorCorreoAAnalistasDelegaEnElPort() {
        reporteService.enviarPorCorreoAAnalistas();

        verify(reportePort, times(1)).enviarReportePorCorreoAAnalistas();
    }
}
