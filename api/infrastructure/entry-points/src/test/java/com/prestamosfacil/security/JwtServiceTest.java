package com.prestamosfacil.security;

import com.prestamosfacil.mocks.PrestamoFacilMocks;
import com.prestamosfacil.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService(
            "clave-de-prueba-suficientemente-larga-para-hs256-1234567890", 3_600_000L);

    @Test
    void generarTokenYValidarloRetornaLosClaimsDelUsuario() {
        Usuario usuario = PrestamoFacilMocks.getMockUsuarioCliente();

        String token = jwtService.generarToken(usuario);

        Claims claims = jwtService.validarYExtraerClaims(token);

        assertEquals("juan@mail.com", claims.getSubject());
        assertEquals(1L, claims.get("id", Long.class));
        assertEquals("CLIENTE", claims.get("rol", String.class));
    }

    @Test
    void validarUnTokenInvalidoLanzaExcepcion() {
        assertThrows(JwtException.class, () -> jwtService.validarYExtraerClaims("esto-no-es-un-token-valido"));
    }

    @Test
    void getExpiracionMsRetornaElValorConfigurado() {
        assertEquals(3_600_000L, jwtService.getExpiracionMs());
    }
}
