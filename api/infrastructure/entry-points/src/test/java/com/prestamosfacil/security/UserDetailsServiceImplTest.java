package com.prestamosfacil.security;

import com.prestamosfacil.mocks.PrestamoFacilMocks;
import com.prestamosfacil.model.Usuario;
import com.prestamosfacil.ports.IUsuarioPersistencePort;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private IUsuarioPersistencePort usuarioPersistencePort;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void loadUserByUsernameConEmailExistenteRetornaUsuarioAutenticado() {
        Usuario usuario = PrestamoFacilMocks.getMockUsuarioCliente();

        when(usuarioPersistencePort.buscarPorEmail("juan@mail.com")).thenReturn(Optional.of(usuario));

        UserDetails userDetails = userDetailsService.loadUserByUsername("juan@mail.com");

        assertEquals("juan@mail.com", userDetails.getUsername());
        assertEquals(1L, ((UsuarioAutenticado) userDetails).getId());
    }

    @Test
    void loadUserByUsernameConEmailInexistenteLanzaExcepcion() {
        when(usuarioPersistencePort.buscarPorEmail("desconocido@mail.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("desconocido@mail.com"));
    }
}
