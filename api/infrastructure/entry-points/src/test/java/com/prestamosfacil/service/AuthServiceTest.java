package com.prestamosfacil.service;

import com.prestamosfacil.application.port.IAutenticacionPort;
import com.prestamosfacil.dto.response.AuthResponse;
import com.prestamosfacil.model.Usuario;
import com.prestamosfacil.security.JwtService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.prestamosfacil.mocks.PrestamoFacilMocks.getMockUsuarioAutenticacion;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private IAutenticacionPort autenticacionPort;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void loginRetornaElTokenGeneradoParaElUsuarioAutenticado() {
        Usuario usuario = getMockUsuarioAutenticacion();

        when(autenticacionPort.autenticar("juan@mail.com", "Clave123!")).thenReturn(usuario);
        when(jwtService.generarToken(usuario)).thenReturn("token-generado");
        when(jwtService.getExpiracionMs()).thenReturn(3_600_000L);

        AuthResponse respuesta = authService.login("juan@mail.com", "Clave123!");

        assertEquals("token-generado", respuesta.token());
        assertEquals("Bearer", respuesta.tipo());
        assertEquals(3_600_000L, respuesta.expiraEnMs());
    }
}
