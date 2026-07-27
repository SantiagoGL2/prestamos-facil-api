package com.prestamosfacil.controller;

import com.prestamosfacil.dto.response.ReporteMontosAprobadosResponse;
import com.prestamosfacil.exceptionHandler.ErrorResponse;
import com.prestamosfacil.service.ReporteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reportes")
@Tag(name = "Reportes", description = "Reporte global de préstamos aprobados")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/prestamos-aprobados")
    @Operation(summary = "Consultar el reporte de préstamos aprobados", description = "Retorna el listado de "
            + "todos los préstamos aprobados en el sistema junto con el monto total acumulado ya calculado. "
            + "Solo accesible para ANALISTA.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reporte generado exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene rol ANALISTA")
    })
    public ResponseEntity<ReporteMontosAprobadosResponse> generarReporte() {
        return ResponseEntity.ok(reporteService.generarReporte());
    }

    @PostMapping("/prestamos-aprobados/enviar-por-correo")
    @Operation(summary = "Enviar el reporte por correo a todos los analistas", description = "Genera el mismo "
            + "reporte de préstamos aprobados en PDF y lo envía de forma asíncrona (RabbitMQ) al correo de cada "
            + "usuario con rol ANALISTA registrado en el sistema — no se envía a un correo puntual. Solo "
            + "accesible para ANALISTA.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Envío encolado exitosamente"),
            @ApiResponse(responseCode = "400", description = "No hay analistas registrados para enviar el reporte",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene rol ANALISTA")
    })
    public ResponseEntity<Void> enviarPorCorreoAAnalistas() {
        reporteService.enviarPorCorreoAAnalistas();

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
