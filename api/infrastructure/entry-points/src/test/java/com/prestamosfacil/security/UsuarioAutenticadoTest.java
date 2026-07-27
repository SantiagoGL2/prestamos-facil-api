package com.prestamosfacil.security;

import com.prestamosfacil.enums.RolUsuario;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

import static com.prestamosfacil.mocks.PrestamoFacilMocks.getMockUsuarioAutenticacion;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UsuarioAutenticadoTest {

    private final UsuarioAutenticado usuarioAutenticado = new UsuarioAutenticado(getMockUsuarioAutenticacion());

    @Test
    void getIdRetornaElIdDelUsuario() {
        assertEquals(1L, usuarioAutenticado.getId());
    }

    @Test
    void getRolRetornaElRolDelUsuario() {
        assertEquals(RolUsuario.ANALISTA, usuarioAutenticado.getRol());
    }

    @Test
    void getUsernameRetornaElEmail() {
        assertEquals("juan@mail.com", usuarioAutenticado.getUsername());
    }

    @Test
    void getPasswordRetornaElHashGuardado() {
        assertEquals("hashGuardado", usuarioAutenticado.getPassword());
    }

    @Test
    void getAuthoritiesRetornaElRolConPrefijoRole() {
        List<GrantedAuthority> authorities = usuarioAutenticado.getAuthorities();

        assertEquals(1, authorities.size());
        assertEquals("ROLE_ANALISTA", authorities.getFirst().getAuthority());
    }
}
