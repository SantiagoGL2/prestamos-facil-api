package com.prestamosfacil.controller;

import com.prestamosfacil.dto.request.ActualizarEstadoSolicitudRequest;
import com.prestamosfacil.dto.request.RegistrarSolicitudRequest;
import com.prestamosfacil.dto.response.PaginaResponse;
import com.prestamosfacil.dto.response.SolicitudPrestamoResponse;
import com.prestamosfacil.enums.EstadoSolicitud;
import com.prestamosfacil.model.SolicitudPrestamo;
import com.prestamosfacil.service.SolicitudPrestamoService;

import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/solicitudes-prestamo")
public class SolicitudPrestamoController {

    private final SolicitudPrestamoService solicitudPrestamoService;

    public SolicitudPrestamoController(SolicitudPrestamoService solicitudPrestamoService) {
        this.solicitudPrestamoService = solicitudPrestamoService;
    }

    @PostMapping
    public ResponseEntity<SolicitudPrestamoResponse> registrar(@Valid @RequestBody RegistrarSolicitudRequest request) {
        SolicitudPrestamo solicitud = solicitudPrestamoService.registrarSolicitud(request.usuarioId(),
                request.tipoPrestamoId(), request.monto(), request.plazoMeses());

        return ResponseEntity.status(HttpStatus.CREATED).body(SolicitudPrestamoResponse.from(solicitud));
    }

    @GetMapping("/por-estado")
    public ResponseEntity<PaginaResponse<SolicitudPrestamoResponse>> listarPorEstado(
            @RequestParam(required = false) EstadoSolicitud estado,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamano) {
        PaginaResponse<SolicitudPrestamoResponse> respuesta = solicitudPrestamoService.listarPorEstado(estado,
                pagina, tamano);

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/por-fecha")
    public ResponseEntity<PaginaResponse<SolicitudPrestamoResponse>> listarPorFecha(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamano) {
        PaginaResponse<SolicitudPrestamoResponse> respuesta = solicitudPrestamoService.listarPorFecha(fechaDesde,
                fechaHasta, pagina, tamano);

        return ResponseEntity.ok(respuesta);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<SolicitudPrestamoResponse> actualizarEstado(@PathVariable Long id,
            @Valid @RequestBody ActualizarEstadoSolicitudRequest request) {
        SolicitudPrestamo solicitud = solicitudPrestamoService.actualizarEstadoManual(id, request.nuevoEstado(),
                request.analistaId());

        return ResponseEntity.ok(SolicitudPrestamoResponse.from(solicitud));
    }
}
