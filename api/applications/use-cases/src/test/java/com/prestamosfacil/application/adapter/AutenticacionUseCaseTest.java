package com.prestamosfacil.application.adapter;

import com.prestamosfacil.exception.CredencialesInvalidasException;
import com.prestamosfacil.model.Usuario;
import com.prestamosfacil.ports.IUsuarioPersistencePort;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.prestamosfacil.application.mocks.PrestamoFacilMocks.getMockUsuarioAutenticacion;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AutenticacionUseCaseTest {

    @Mock
    private IUsuarioPersistencePort usuarioPersistencePort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AutenticacionUseCase autenticacionUseCase;

    @Test
    void credencialesCorrectasRetornaElUsuario() {
        Usuario usuario = getMockUsuarioAutenticacion();

                when(usuarioPersistencePort.buscarPorEmail("juan@mail.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("Clave123!", "hashGuardado")).thenReturn(true);

        Usuario resultado = autenticacionUseCase.autenticar("juan@mail.com", "Clave123!");

        assertEquals(usuario, resultado);
    }

    @Test
    void emailNoRegistradoLanzaExcepcion() {
        when(usuarioPersistencePort.buscarPorEmail("desconocido@mail.com")).thenReturn(Optional.empty());

        assertThrows(CredencialesInvalidasException.class,
                () -> autenticacionUseCase.autenticar("desconocido@mail.com", "Clave123!"));
    }

    @Test
    void passwordNoCoincideLanzaExcepcion() {
        Usuario usuario = getMockUsuarioAutenticacion();

        when(usuarioPersistencePort.buscarPorEmail("juan@mail.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("ClaveIncorrecta", "hashGuardado")).thenReturn(false);

        assertThrows(CredencialesInvalidasException.class,
                () -> autenticacionUseCase.autenticar("juan@mail.com", "ClaveIncorrecta"));
    }
}
