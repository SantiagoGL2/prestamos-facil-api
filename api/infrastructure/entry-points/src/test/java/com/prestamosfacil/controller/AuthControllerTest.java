package com.prestamosfacil.controller;

import com.prestamosfacil.dto.request.LoginRequest;
import com.prestamosfacil.dto.response.AuthResponse;
import com.prestamosfacil.service.AuthService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void loginRetorna200ConElTokenGenerado() {
        AuthResponse authResponse = new AuthResponse("token-generado", "Bearer", 3_600_000L);
        when(authService.login("juan@mail.com", "Clave123!")).thenReturn(authResponse);

        ResponseEntity<AuthResponse> respuesta = authController.login(
                new LoginRequest("juan@mail.com", "Clave123!"));

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(authResponse, respuesta.getBody());
    }
}
