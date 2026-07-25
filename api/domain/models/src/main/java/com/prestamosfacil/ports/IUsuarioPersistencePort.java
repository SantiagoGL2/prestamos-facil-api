package com.prestamosfacil.ports;

import com.prestamosfacil.model.Usuario;

import java.util.Optional;

public interface IUsuarioPersistencePort {

    Usuario guardar(Usuario usuario);

    Optional<Usuario> buscarPorId(Long id);

    Optional<Usuario> buscarPorEmail(String email);

    boolean existeEmail(String email);
}
