package com.prestamosfacil.dto.request;

import com.prestamosfacil.enums.EstadoSolicitud;

import jakarta.validation.constraints.NotNull;

public record ActualizarEstadoSolicitudRequest(

        @NotNull(message = "El nuevo estado es obligatorio")
        EstadoSolicitud nuevoEstado) {
}
