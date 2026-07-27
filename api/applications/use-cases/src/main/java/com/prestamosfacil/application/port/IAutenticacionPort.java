package com.prestamosfacil.application.port;

import com.prestamosfacil.model.Usuario;

public interface IAutenticacionPort {

    Usuario autenticar(String email, String passwordPlano);
}
