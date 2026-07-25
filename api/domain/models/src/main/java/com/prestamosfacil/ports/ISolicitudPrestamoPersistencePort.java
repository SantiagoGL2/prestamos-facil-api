package com.prestamosfacil.ports;

import com.prestamosfacil.model.SolicitudPrestamo;
import com.prestamosfacil.model.pagination.Paginacion;
import com.prestamosfacil.model.pagination.ResultadoPaginado;
import com.prestamosfacil.enums.EstadoSolicitud;

import java.util.Optional;

public interface ISolicitudPrestamoPersistencePort {

    SolicitudPrestamo guardar(SolicitudPrestamo solicitudPrestamo);

    Optional<SolicitudPrestamo> buscarPorId(Long id);

    ResultadoPaginado<SolicitudPrestamo> listarPaginado(EstadoSolicitud estadoOpcional, Paginacion paginacion);
}
