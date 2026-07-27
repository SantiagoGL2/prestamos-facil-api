package com.prestamosfacil.service;

import com.prestamosfacil.application.port.ISolicitudPrestamoPort;
import com.prestamosfacil.dto.response.PaginaResponse;
import com.prestamosfacil.dto.response.SolicitudPrestamoResponse;
import com.prestamosfacil.enums.EstadoSolicitud;
import com.prestamosfacil.model.SolicitudPrestamo;
import com.prestamosfacil.model.pagination.Paginacion;
import com.prestamosfacil.model.pagination.ResultadoPaginado;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class SolicitudPrestamoService {

    private final ISolicitudPrestamoPort solicitudPrestamoPort;

    public SolicitudPrestamoService(ISolicitudPrestamoPort solicitudPrestamoPort) {
        this.solicitudPrestamoPort = solicitudPrestamoPort;
    }

    @Transactional(rollbackFor = Exception.class)
    public SolicitudPrestamo registrarSolicitud(Long usuarioId, Long tipoPrestamoId, BigDecimal monto,
                                                 int plazoMeses) {
        return solicitudPrestamoPort.registrarSolicitud(usuarioId, tipoPrestamoId, monto, plazoMeses);
    }

    @Transactional(readOnly = true)
    public PaginaResponse<SolicitudPrestamoResponse> listarPorEstado(EstadoSolicitud estadoOpcional, int pagina,
                                                                      int tamano) {
        Paginacion paginacion = new Paginacion(pagina, tamano);
        ResultadoPaginado<SolicitudPrestamo> resultado = solicitudPrestamoPort.listarPorEstado(estadoOpcional,
                paginacion);

        return mapearPagina(resultado);
    }

    @Transactional(readOnly = true)
    public PaginaResponse<SolicitudPrestamoResponse> listarPorFecha(LocalDate fechaDesdeOpcional,
                                                                     LocalDate fechaHastaOpcional, int pagina,
                                                                     int tamano) {
        Paginacion paginacion = new Paginacion(pagina, tamano);
        ResultadoPaginado<SolicitudPrestamo> resultado = solicitudPrestamoPort.listarPorFecha(fechaDesdeOpcional,
                fechaHastaOpcional, paginacion);

        return mapearPagina(resultado);
    }

    @Transactional(rollbackFor = Exception.class)
    public SolicitudPrestamo actualizarEstadoManual(Long solicitudId, EstadoSolicitud nuevoEstado, Long analistaId) {
        return solicitudPrestamoPort.actualizarEstadoManual(solicitudId, nuevoEstado, analistaId);
    }

    private PaginaResponse<SolicitudPrestamoResponse> mapearPagina(ResultadoPaginado<SolicitudPrestamo> resultado) {
        return new PaginaResponse<>(
                resultado.contenido().stream().map(SolicitudPrestamoResponse::from).toList(),
                resultado.totalElementos(), resultado.totalPaginas(), resultado.paginaActual());
    }
}
