package com.prestamosfacil.service;

import com.prestamosfacil.application.port.IUsuarioPort;
import com.prestamosfacil.mocks.PrestamoFacilMocks;
import com.prestamosfacil.model.Usuario;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private IUsuarioPort usuarioPort;

    @InjectMocks
    private UsuarioService usuarioService;


    @Test
    void registrarUsuarioDelegaEnElPort() {
        Usuario usuario = PrestamoFacilMocks.getMockUsuarioCliente();

        when(usuarioPort.registrarUsuario("Juan", "Perez", "juan@mail.com", 1L, "123",
                BigDecimal.valueOf(2_000_000), "Clave123!")).thenReturn(usuario);

        Usuario resultado = usuarioService.registrarUsuario("Juan", "Perez", "juan@mail.com", 1L, "123",
                BigDecimal.valueOf(2_000_000), "Clave123!");

        assertEquals(usuario, resultado);
    }

    @Test
    void registrarAnalistaDelegaEnElPort() {
        Usuario usuario = PrestamoFacilMocks.getMockUsuarioCliente();

        when(usuarioPort.registrarAnalista("Ana", "Torres", "ana@mail.com", 1L, "456",
                BigDecimal.valueOf(3_000_000), "Clave123!")).thenReturn(usuario);

        Usuario resultado = usuarioService.registrarAnalista("Ana", "Torres", "ana@mail.com", 1L, "456",
                BigDecimal.valueOf(3_000_000), "Clave123!");

        assertEquals(usuario, resultado);
    }

    @Test
    void buscarPorIdDelegaEnElPort() {
        Usuario usuario = PrestamoFacilMocks.getMockUsuarioCliente();

        when(usuarioPort.buscarPorId(1L)).thenReturn(usuario);

        Usuario resultado = usuarioService.buscarPorId(1L);

        assertEquals(usuario, resultado);
    }

    @Test
    void buscarPorTipoDocumentoYNumeroDocumentoDelegaEnElPort() {
        Usuario usuario = PrestamoFacilMocks.getMockUsuarioCliente();

        when(usuarioPort.buscarPorTipoDocumentoYNumeroDocumento(1L, "123")).thenReturn(usuario);

        Usuario resultado = usuarioService.buscarPorTipoDocumentoYNumeroDocumento(1L, "123");

        assertEquals(usuario, resultado);
    }

    @Test
    void listarClientesDelegaEnElPort() {
        Usuario usuario = PrestamoFacilMocks.getMockUsuarioCliente();

        when(usuarioPort.listarClientes()).thenReturn(List.of(usuario));

        List<Usuario> resultado = usuarioService.listarClientes();

        assertEquals(List.of(usuario), resultado);
    }

    @Test
    void listarAnalistasDelegaEnElPort() {
        Usuario usuario = PrestamoFacilMocks.getMockUsuarioCliente();

        when(usuarioPort.listarAnalistas()).thenReturn(List.of(usuario));

        List<Usuario> resultado = usuarioService.listarAnalistas();

        assertEquals(List.of(usuario), resultado);
    }
}
