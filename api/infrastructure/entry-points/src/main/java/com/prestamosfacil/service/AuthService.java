package com.prestamosfacil.service;

import com.prestamosfacil.application.port.IAutenticacionPort;
import com.prestamosfacil.dto.response.AuthResponse;
import com.prestamosfacil.model.Usuario;
import com.prestamosfacil.security.JwtService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final IAutenticacionPort autenticacionPort;
    private final JwtService jwtService;

    public AuthService(IAutenticacionPort autenticacionPort, JwtService jwtService) {
        this.autenticacionPort = autenticacionPort;
        this.jwtService = jwtService;
    }

    @Transactional(readOnly = true)
    public AuthResponse login(String email, String password) {
        Usuario usuario = autenticacionPort.autenticar(email, password);
        String token = jwtService.generarToken(usuario);

        return new AuthResponse(token, "Bearer", jwtService.getExpiracionMs());
    }
}
