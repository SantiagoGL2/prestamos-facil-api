package com.prestamosfacil.model;

public record NotificacionRegistroEvento(Long usuarioId, String usuarioNombreCompleto, String usuarioEmail,
                                          String rol) {
}
