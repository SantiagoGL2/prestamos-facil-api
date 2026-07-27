package com.prestamosfacil.controller;

import com.prestamosfacil.dto.request.ActualizarEstadoSolicitudRequest;
import com.prestamosfacil.dto.request.RegistrarSolicitudRequest;
import com.prestamosfacil.dto.response.PaginaResponse;
import com.prestamosfacil.dto.response.SolicitudPrestamoResponse;
import com.prestamosfacil.enums.EstadoSolicitud;
import com.prestamosfacil.mocks.PrestamoFacilMocks;
import com.prestamosfacil.model.SolicitudPrestamo;
import com.prestamosfacil.model.TipoPrestamo;
import com.prestamosfacil.model.Usuario;
import com.prestamosfacil.security.UsuarioAutenticado;
import com.prestamosfacil.service.SolicitudPrestamoService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolicitudPrestamoControllerTest {

    @Mock
    private SolicitudPrestamoService solicitudPrestamoService;

    @InjectMocks
    private SolicitudPrestamoController solicitudPrestamoController;

    @Test
    void registrarUsaElIdDelPrincipalYRetorna201() {

        Usuario cliente = PrestamoFacilMocks.getMockUsuarioCliente();
        SolicitudPrestamo solicitud = PrestamoFacilMocks.getMockSolicitudPrestamo();

        when(solicitudPrestamoService.registrarSolicitud(1L, 1L, BigDecimal.valueOf(5_000_000), 12))
                .thenReturn(solicitud);

        RegistrarSolicitudRequest request = new RegistrarSolicitudRequest(1L, BigDecimal.valueOf(5_000_000), 12);
        ResponseEntity<SolicitudPrestamoResponse> respuesta = solicitudPrestamoController.registrar(request,
                new UsuarioAutenticado(cliente));

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertEquals(solicitud.id(), respuesta.getBody().id());
    }

    @Test
    void listarPorEstadoRetorna200ConLaPaginaObtenida() {
        SolicitudPrestamo solicitud = PrestamoFacilMocks.getMockSolicitudPrestamo();

        PaginaResponse<SolicitudPrestamoResponse> pagina = new PaginaResponse<>(
                java.util.List.of(SolicitudPrestamoResponse.from(solicitud)), 1, 1, 0);
        when(solicitudPrestamoService.listarPorEstado(EstadoSolicitud.PENDIENTE_REVISION, 0, 20)).thenReturn(pagina);

        ResponseEntity<PaginaResponse<SolicitudPrestamoResponse>> respuesta =
                solicitudPrestamoController.listarPorEstado(EstadoSolicitud.PENDIENTE_REVISION, 0, 20);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(pagina, respuesta.getBody());
    }

    @Test
    void listarPorFechaRetorna200ConLaPaginaObtenida() {

        SolicitudPrestamo solicitud = PrestamoFacilMocks.getMockSolicitudPrestamo();

        LocalDate fechaDesde = LocalDate.now().minusDays(10);
        LocalDate fechaHasta = LocalDate.now();
        PaginaResponse<SolicitudPrestamoResponse> pagina = new PaginaResponse<>(
                java.util.List.of(SolicitudPrestamoResponse.from(solicitud)), 1, 1, 0);
        when(solicitudPrestamoService.listarPorFecha(fechaDesde, fechaHasta, 0, 20)).thenReturn(pagina);

        ResponseEntity<PaginaResponse<SolicitudPrestamoResponse>> respuesta =
                solicitudPrestamoController.listarPorFecha(fechaDesde, fechaHasta, 0, 20);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(pagina, respuesta.getBody());
    }

    @Test
    void actualizarEstadoUsaElIdDelAnalistaAutenticado() {
        Usuario cliente = PrestamoFacilMocks.getMockUsuarioCliente();
        TipoPrestamo tipoPrestamo = PrestamoFacilMocks.getMockTipoPrestamo();
        Usuario analista = PrestamoFacilMocks.getMockUsuarioAnalista();

        SolicitudPrestamo resuelta = new SolicitudPrestamo(100L, cliente, tipoPrestamo,
                BigDecimal.valueOf(5_000_000), 12, EstadoSolicitud.APROBADO, 5L, LocalDateTime.now(),
                LocalDateTime.now());
        when(solicitudPrestamoService.actualizarEstadoManual(100L, EstadoSolicitud.APROBADO, 5L))
                .thenReturn(resuelta);

        ResponseEntity<SolicitudPrestamoResponse> respuesta = solicitudPrestamoController.actualizarEstado(100L,
                new ActualizarEstadoSolicitudRequest(EstadoSolicitud.APROBADO), new UsuarioAutenticado(analista));

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals("APROBADO", respuesta.getBody().estado());
    }
}
