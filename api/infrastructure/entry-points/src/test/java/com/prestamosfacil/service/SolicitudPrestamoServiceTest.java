package com.prestamosfacil.service;

import com.prestamosfacil.application.port.ISolicitudPrestamoPort;
import com.prestamosfacil.dto.response.PaginaResponse;
import com.prestamosfacil.dto.response.SolicitudPrestamoResponse;
import com.prestamosfacil.enums.EstadoSolicitud;
import com.prestamosfacil.mocks.PrestamoFacilMocks;
import com.prestamosfacil.model.SolicitudPrestamo;
import com.prestamosfacil.model.TipoPrestamo;
import com.prestamosfacil.model.Usuario;
import com.prestamosfacil.model.pagination.Paginacion;
import com.prestamosfacil.model.pagination.ResultadoPaginado;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolicitudPrestamoServiceTest {

    @Mock
    private ISolicitudPrestamoPort solicitudPrestamoPort;

    @InjectMocks
    private SolicitudPrestamoService solicitudPrestamoService;


    @Test
    void registrarSolicitudDelegaEnElPort() {
        SolicitudPrestamo solicitud = PrestamoFacilMocks.getMockSolicitudPrestamo();

        when(solicitudPrestamoPort.registrarSolicitud(1L, 1L, BigDecimal.valueOf(5_000_000), 12))
                .thenReturn(solicitud);

        SolicitudPrestamo resultado = solicitudPrestamoService.registrarSolicitud(1L, 1L,
                BigDecimal.valueOf(5_000_000), 12);

        assertEquals(solicitud, resultado);
    }

    @Test
    void listarPorEstadoMapeaElResultadoPaginado() {
        SolicitudPrestamo solicitud = PrestamoFacilMocks.getMockSolicitudPrestamo();

        ResultadoPaginado<SolicitudPrestamo> resultadoPaginado = new ResultadoPaginado<>(List.of(solicitud), 1, 1, 0);
        when(solicitudPrestamoPort.listarPorEstado(EstadoSolicitud.PENDIENTE_REVISION, new Paginacion(0, 20)))
                .thenReturn(resultadoPaginado);

        PaginaResponse<SolicitudPrestamoResponse> respuesta = solicitudPrestamoService.listarPorEstado(
                EstadoSolicitud.PENDIENTE_REVISION, 0, 20);

        assertEquals(1, respuesta.contenido().size());
        assertEquals(solicitud.id(), respuesta.contenido().get(0).id());
        assertEquals(1, respuesta.totalElementos());
        assertEquals(1, respuesta.totalPaginas());
        assertEquals(0, respuesta.paginaActual());
    }

    @Test
    void listarPorFechaMapeaElResultadoPaginado() {
        SolicitudPrestamo solicitud = PrestamoFacilMocks.getMockSolicitudPrestamo();

        LocalDate fechaDesde = LocalDate.now().minusDays(10);
        LocalDate fechaHasta = LocalDate.now();
        ResultadoPaginado<SolicitudPrestamo> resultadoPaginado = new ResultadoPaginado<>(List.of(solicitud), 1, 1, 0);
        when(solicitudPrestamoPort.listarPorFecha(fechaDesde, fechaHasta, new Paginacion(0, 20)))
                .thenReturn(resultadoPaginado);

        PaginaResponse<SolicitudPrestamoResponse> respuesta = solicitudPrestamoService.listarPorFecha(fechaDesde,
                fechaHasta, 0, 20);

        assertEquals(1, respuesta.contenido().size());
        assertEquals(solicitud.id(), respuesta.contenido().get(0).id());
    }

    @Test
    void actualizarEstadoManualDelegaEnElPort() {
        Usuario usuario = PrestamoFacilMocks.getMockUsuarioCliente();
        TipoPrestamo tipoPrestamo = PrestamoFacilMocks.getMockTipoPrestamo();

        SolicitudPrestamo resuelta = new SolicitudPrestamo(100L, usuario, tipoPrestamo,
                BigDecimal.valueOf(5_000_000), 12, EstadoSolicitud.APROBADO, 5L, LocalDateTime.now(),
                LocalDateTime.now());
        when(solicitudPrestamoPort.actualizarEstadoManual(100L, EstadoSolicitud.APROBADO, 5L)).thenReturn(resuelta);

        SolicitudPrestamo resultado = solicitudPrestamoService.actualizarEstadoManual(100L, EstadoSolicitud.APROBADO,
                5L);

        assertEquals(resuelta, resultado);
    }
}
