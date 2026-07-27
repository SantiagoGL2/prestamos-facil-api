package com.prestamosfacil.controller;

import com.prestamosfacil.dto.response.TipoPrestamoResponse;
import com.prestamosfacil.mocks.PrestamoFacilMocks;
import com.prestamosfacil.model.TipoPrestamo;
import com.prestamosfacil.service.TipoPrestamoService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TipoPrestamoControllerTest {

    @Mock
    private TipoPrestamoService tipoPrestamoService;

    @InjectMocks
    private TipoPrestamoController tipoPrestamoController;

    @Test
    void listarRetorna200ConElListadoMapeado() {
        TipoPrestamo tipoPrestamo = PrestamoFacilMocks.getMockTipoPrestamo();

        when(tipoPrestamoService.listarActivos()).thenReturn(List.of(tipoPrestamo));

        ResponseEntity<List<TipoPrestamoResponse>> respuesta = tipoPrestamoController.listar();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        assertEquals(tipoPrestamo.id(), respuesta.getBody().getFirst().id());
    }

    @Test
    void obtenerRetorna200ConElTipoPrestamoEncontrado() {
        TipoPrestamo tipoPrestamo = PrestamoFacilMocks.getMockTipoPrestamo();

        when(tipoPrestamoService.buscarPorId(1L)).thenReturn(tipoPrestamo);

        ResponseEntity<TipoPrestamoResponse> respuesta = tipoPrestamoController.obtener(1L);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(tipoPrestamo.id(), respuesta.getBody().id());
    }
}
