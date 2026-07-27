package com.prestamosfacil.security;

import com.prestamosfacil.model.Usuario;
import com.prestamosfacil.ports.IUsuarioPersistencePort;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IUsuarioPersistencePort usuarioPersistencePort;

    public UserDetailsServiceImpl(IUsuarioPersistencePort usuarioPersistencePort) {
        this.usuarioPersistencePort = usuarioPersistencePort;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioPersistencePort.buscarPorEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No existe un usuario con el email: " + email));

        return new UsuarioAutenticado(usuario);
    }
}
