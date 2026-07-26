package com.prestamosfacil.dto.response;

import com.prestamosfacil.model.Usuario;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UsuarioResponse(Long id, String nombres, String apellidos, String email,
                               TipoDocumentoResponse tipoDocumento, String numeroDocumento,
                               BigDecimal salarioBase, String rol, LocalDateTime fechaCreacion) {

    public static UsuarioResponse from(Usuario usuario) {
        return new UsuarioResponse(usuario.id(), usuario.nombres(), usuario.apellidos(), usuario.email(),
                TipoDocumentoResponse.from(usuario.tipoDocumento()), usuario.numeroDocumento(),
                usuario.salarioBase(), usuario.rol().name(), usuario.fechaCreacion());
    }
}
