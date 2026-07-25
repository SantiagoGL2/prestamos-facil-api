package com.prestamosfacil.model;

import com.prestamosfacil.enums.EstadoNotificacion;

import java.time.LocalDateTime;

public record Notificacion(Long id, Long usuarioId, Long solicitudId, String tipo, String mensaje,
                            EstadoNotificacion estadoEnvio, LocalDateTime fechaEnvio) {
}
