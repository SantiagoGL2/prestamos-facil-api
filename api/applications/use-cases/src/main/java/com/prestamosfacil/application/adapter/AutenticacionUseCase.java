package com.prestamosfacil.application.adapter;

import com.prestamosfacil.application.port.IAutenticacionPort;
import com.prestamosfacil.exception.CredencialesInvalidasException;
import com.prestamosfacil.model.Usuario;
import com.prestamosfacil.ports.IUsuarioPersistencePort;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AutenticacionUseCase implements IAutenticacionPort {

    private static final String MENSAJE_CREDENCIALES_INVALIDAS = "Credenciales inválidas";

    private final IUsuarioPersistencePort usuarioPersistencePort;
    private final PasswordEncoder passwordEncoder;

    public AutenticacionUseCase(IUsuarioPersistencePort usuarioPersistencePort, PasswordEncoder passwordEncoder) {
        this.usuarioPersistencePort = usuarioPersistencePort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario autenticar(String email, String passwordPlano) {
        Usuario usuario = usuarioPersistencePort.buscarPorEmail(email)
                .orElseThrow(() -> new CredencialesInvalidasException(MENSAJE_CREDENCIALES_INVALIDAS));

        if (!passwordEncoder.matches(passwordPlano, usuario.passwordHash())) {
            throw new CredencialesInvalidasException(MENSAJE_CREDENCIALES_INVALIDAS);
        }

        return usuario;
    }
}
