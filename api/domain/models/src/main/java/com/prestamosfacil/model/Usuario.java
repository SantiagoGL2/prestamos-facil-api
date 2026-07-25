package com.prestamosfacil.model;

import com.prestamosfacil.enums.RolUsuario;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Usuario(Long id, String nombres, String apellidos, String email, TipoDocumento tipoDocumento,
                       String numeroDocumento, BigDecimal salarioBase, String passwordHash, RolUsuario rol,
                       LocalDateTime fechaCreacion) {
}
