package com.prestamosfacil.controller;

import com.prestamosfacil.dto.request.RegistrarSolicitudRequest;
import com.prestamosfacil.dto.response.SolicitudPrestamoResponse;
import com.prestamosfacil.model.SolicitudPrestamo;
import com.prestamosfacil.service.SolicitudPrestamoService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
