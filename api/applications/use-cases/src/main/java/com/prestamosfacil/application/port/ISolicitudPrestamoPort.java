package com.prestamosfacil.application.port;

import com.prestamosfacil.enums.EstadoSolicitud;
import com.prestamosfacil.model.SolicitudPrestamo;
import com.prestamosfacil.model.pagination.Paginacion;
import com.prestamosfacil.model.pagination.ResultadoPaginado;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ISolicitudPrestamoPort {

    SolicitudPrestamo registrarSolicitud(Long usuarioId, Long tipoPrestamoId, BigDecimal monto, int plazoMeses);

    ResultadoPaginado<SolicitudPrestamo> listarPorEstado(EstadoSolicitud estadoOpcional, Paginacion paginacion);

    ResultadoPaginado<SolicitudPrestamo> listarPorFecha(LocalDate fechaDesdeOpcional, LocalDate fechaHastaOpcional,
                                                         Paginacion paginacion);

    SolicitudPrestamo actualizarEstadoManual(Long solicitudId, EstadoSolicitud nuevoEstado, Long analistaId);
}
