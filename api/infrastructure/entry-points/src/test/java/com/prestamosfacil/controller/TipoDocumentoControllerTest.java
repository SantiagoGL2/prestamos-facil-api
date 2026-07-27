package com.prestamosfacil.controller;

import com.prestamosfacil.dto.response.TipoDocumentoResponse;
import com.prestamosfacil.model.TipoDocumento;
import com.prestamosfacil.service.TipoDocumentoService;

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
class TipoDocumentoControllerTest {

    @Mock
    private TipoDocumentoService tipoDocumentoService;

    @InjectMocks
    private TipoDocumentoController tipoDocumentoController;

    @Test
    void listarRetorna200ConElListadoMapeado() {
        when(tipoDocumentoService.listarActivos()).thenReturn(
                List.of(new TipoDocumento(1L, "CC", "Cedula de ciudadania", true)));

        ResponseEntity<List<TipoDocumentoResponse>> respuesta = tipoDocumentoController.listar();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        assertEquals("CC", respuesta.getBody().getFirst().codigo());
    }
}
