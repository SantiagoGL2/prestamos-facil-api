package com.prestamosfacil.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record RegistrarUsuarioRequest(

        @NotBlank(message = "Los nombres son obligatorios")
        @Size(max = 100, message = "Los nombres no pueden superar los 100 caracteres")
        String nombres,

        @NotBlank(message = "Los apellidos son obligatorios")
        @Size(max = 100, message = "Los apellidos no pueden superar los 100 caracteres")
        String apellidos,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email debe tener un formato valido")
        @Size(max = 150, message = "El email no puede superar los 150 caracteres")
        String email,

        @NotNull(message = "El tipo de documento es obligatorio")
        Long tipoDocumentoId,

        @NotBlank(message = "El numero de documento es obligatorio")
        @Size(max = 30, message = "El numero de documento no puede superar los 30 caracteres")
        String numeroDocumento,

        @NotNull(message = "El salario base es obligatorio")
        BigDecimal salarioBase,

        @NotBlank(message = "El password es obligatorio")
        @Size(min = 8, max = 100, message = "El password debe tener entre 8 y 100 caracteres")
        String password) {
}
