package com.prestamosfacil.application.adapter;

import com.prestamosfacil.enums.RolUsuario;
import com.prestamosfacil.exception.SolicitudInvalidaException;
import com.prestamosfacil.model.ReporteMontosAprobados;
import com.prestamosfacil.model.ReportePrestamoAprobado;
import com.prestamosfacil.ports.IPrestamoPersistencePort;
import com.prestamosfacil.ports.IReportePublisherPort;
import com.prestamosfacil.ports.IUsuarioPersistencePort;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static com.prestamosfacil.application.mocks.PrestamoFacilMocks.getMockUsuarioAnalista;
import static com.prestamosfacil.application.mocks.PrestamoFacilMocks.getMocksReportePrestamoAprobado;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReporteUseCaseTest {

    @Mock
    private IPrestamoPersistencePort prestamoPersistencePort;

    @Mock
    private IUsuarioPersistencePort usuarioPersistencePort;

    @Mock
    private IReportePublisherPort reportePublisherPort;

    @InjectMocks
    private ReporteUseCase reporteUseCase;

    @Test
    void generarReporteMontosAprobadosSumaCorrectamenteLosMontos() {

        List<ReportePrestamoAprobado> prestamos = getMocksReportePrestamoAprobado();

        when(prestamoPersistencePort.listarAprobados()).thenReturn(prestamos);

        ReporteMontosAprobados reporte = reporteUseCase.generarReporteMontosAprobados();

        assertEquals(0, reporte.montoTotalAprobado().compareTo(BigDecimal.valueOf(3_500_000)));
    }

    @Test
    void enviarReportePorCorreoAAnalistasPublicaUnEventoPorCadaAnalista() {
        when(prestamoPersistencePort.listarAprobados()).thenReturn(List.of());
        when(usuarioPersistencePort.listarPorRol(RolUsuario.ANALISTA)).thenReturn(List.of(getMockUsuarioAnalista()));

        reporteUseCase.enviarReportePorCorreoAAnalistas();

        verify(reportePublisherPort, times(1)).publicarReporteParaEnvio(any());
    }

    @Test
    void enviarReportePorCorreoSinAnalistasLanzaExcepcion() {
        when(prestamoPersistencePort.listarAprobados()).thenReturn(List.of());
        when(usuarioPersistencePort.listarPorRol(RolUsuario.ANALISTA)).thenReturn(List.of());

        assertThrows(SolicitudInvalidaException.class, () -> reporteUseCase.enviarReportePorCorreoAAnalistas());
    }
}
