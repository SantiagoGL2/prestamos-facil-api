package com.prestamosfacil.dto.response;

public record AuthResponse(String token, String tipo, Long expiraEnMs) {
}
