package com.prestamosfacil.controller;

import com.prestamosfacil.dto.response.ReporteMontosAprobadosResponse;
import com.prestamosfacil.service.ReporteService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/prestamos-aprobados")
    public ResponseEntity<ReporteMontosAprobadosResponse> generarReporte() {
        return ResponseEntity.ok(reporteService.generarReporte());
    }

    @PostMapping("/prestamos-aprobados/enviar-por-correo")
    public ResponseEntity<Void> enviarPorCorreoAAnalistas() {
        reporteService.enviarPorCorreoAAnalistas();

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
