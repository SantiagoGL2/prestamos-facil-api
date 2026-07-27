package com.prestamosfacil.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthResponse(
        String token,

        @Schema(description = "Esquema de autenticación a usar en el header Authorization, siempre \"Bearer\"")
        String tipo,

        @Schema(description = "Tiempo de vida del token en milisegundos, a partir del momento en que se emitió")
        Long expiraEnMs) {
}
