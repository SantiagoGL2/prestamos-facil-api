package com.prestamosfacil.controller;

import com.prestamosfacil.dto.request.RegistrarUsuarioRequest;
import com.prestamosfacil.dto.response.UsuarioResponse;
import com.prestamosfacil.mocks.PrestamoFacilMocks;
import com.prestamosfacil.model.Usuario;
import com.prestamosfacil.service.UsuarioService;

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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalistaControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private AnalistaController analistaController;

    @Test
    void registrarRetorna201ConElAnalistaCreado() {

        Usuario analista = PrestamoFacilMocks.getMockUsuarioAnalista();

        when(usuarioService.registrarAnalista("Ana", "Torres", "ana@mail.com", 1L, "999",
                BigDecimal.valueOf(3_000_000), "Clave123!")).thenReturn(analista);

        RegistrarUsuarioRequest request = new RegistrarUsuarioRequest("Ana", "Torres", "ana@mail.com", 1L, "999",
                BigDecimal.valueOf(3_000_000), "Clave123!");
        ResponseEntity<UsuarioResponse> respuesta = analistaController.registrar(request);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertEquals(analista.id(), respuesta.getBody().id());
    }

    @Test
    void listarAnalistasRetorna200ConElListadoMapeado() {

        Usuario analista = PrestamoFacilMocks.getMockUsuarioAnalista();

        when(usuarioService.listarAnalistas()).thenReturn(List.of(analista));

        ResponseEntity<List<UsuarioResponse>> respuesta = analistaController.listarAnalistas();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        assertEquals(analista.id(), respuesta.getBody().get(0).id());
    }
}
