package com.prestamosfacil.ports;

import com.prestamosfacil.model.SolicitudPrestamo;
import com.prestamosfacil.model.pagination.Paginacion;
import com.prestamosfacil.model.pagination.ResultadoPaginado;
import com.prestamosfacil.enums.EstadoSolicitud;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Puerto de persistencia para las solicitudes de préstamo. Cubre tanto el guardado (alta y
 * transiciones de estado) como las dos consultas paginadas que la API expone por separado:
 * por estado y por rango de fechas — quien implemente este puerto debe soportar ambos filtros
 * de forma independiente, sin combinarlos.
 */
public interface ISolicitudPrestamoPersistencePort {

    SolicitudPrestamo guardar(SolicitudPrestamo solicitudPrestamo);

    Optional<SolicitudPrestamo> buscarPorId(Long id);

    ResultadoPaginado<SolicitudPrestamo> listarPorEstado(EstadoSolicitud estadoOpcional, Paginacion paginacion);

    ResultadoPaginado<SolicitudPrestamo> listarPorFecha(LocalDate fechaDesdeOpcional, LocalDate fechaHastaOpcional,
                                                         Paginacion paginacion);
}
