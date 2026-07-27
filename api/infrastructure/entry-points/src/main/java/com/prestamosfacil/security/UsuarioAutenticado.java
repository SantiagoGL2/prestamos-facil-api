package com.prestamosfacil.security;

import com.prestamosfacil.enums.RolUsuario;
import com.prestamosfacil.model.Usuario;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public class UsuarioAutenticado implements UserDetails {

    private final Usuario usuario;

    public UsuarioAutenticado(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getId() {
        return usuario.id();
    }

    public RolUsuario getRol() {
        return usuario.rol();
    }

    @Override
    public String getUsername() {
        return usuario.email();
    }

    @Override
    public String getPassword() {
        return usuario.passwordHash();
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + usuario.rol().name()));
    }
}
