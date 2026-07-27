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
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    @Test
    void registrarRetorna201ConElUsuarioCreado() {
        Usuario usuario = PrestamoFacilMocks.getMockUsuarioCliente();

        when(usuarioService.registrarUsuario("Juan", "Perez", "juan@mail.com", 1L, "123",
                BigDecimal.valueOf(2_000_000), "Clave123!")).thenReturn(usuario);

        RegistrarUsuarioRequest request = new RegistrarUsuarioRequest("Juan", "Perez", "juan@mail.com", 1L, "123",
                BigDecimal.valueOf(2_000_000), "Clave123!");
        ResponseEntity<UsuarioResponse> respuesta = usuarioController.registrar(request);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertEquals(usuario.id(), respuesta.getBody().id());
    }

    @Test
    void listarClientesRetorna200ConElListadoMapeado() {
        Usuario usuario = PrestamoFacilMocks.getMockUsuarioCliente();

        when(usuarioService.listarClientes()).thenReturn(List.of(usuario));

        ResponseEntity<List<UsuarioResponse>> respuesta = usuarioController.listarClientes();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        assertEquals(usuario.id(), respuesta.getBody().getFirst().id());
    }

    @Test
    void obtenerRetorna200ConElUsuarioEncontrado() {
        Usuario usuario = PrestamoFacilMocks.getMockUsuarioCliente();

        when(usuarioService.buscarPorId(1L)).thenReturn(usuario);

        ResponseEntity<UsuarioResponse> respuesta = usuarioController.obtener(1L);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(usuario.id(), respuesta.getBody().id());
    }

    @Test
    void obtenerPorTipoDocumentoYNumeroDocumentoRetorna200() {
        Usuario usuario = PrestamoFacilMocks.getMockUsuarioCliente();

        when(usuarioService.buscarPorTipoDocumentoYNumeroDocumento(1L, "123")).thenReturn(usuario);

        ResponseEntity<UsuarioResponse> respuesta = usuarioController.obtenerPorTipoDocumentoYNumeroDocumento(1L,
                "123");

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(usuario.id(), respuesta.getBody().id());
    }
}
