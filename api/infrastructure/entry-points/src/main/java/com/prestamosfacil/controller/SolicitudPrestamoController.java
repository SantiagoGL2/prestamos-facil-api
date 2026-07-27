package com.prestamosfacil.controller;

import com.prestamosfacil.dto.request.ActualizarEstadoSolicitudRequest;
import com.prestamosfacil.dto.request.RegistrarSolicitudRequest;
import com.prestamosfacil.dto.response.PaginaResponse;
import com.prestamosfacil.dto.response.SolicitudPrestamoResponse;
import com.prestamosfacil.enums.EstadoSolicitud;
import com.prestamosfacil.exceptionHandler.ErrorResponse;
import com.prestamosfacil.model.SolicitudPrestamo;
import com.prestamosfacil.security.UsuarioAutenticado;
import com.prestamosfacil.service.SolicitudPrestamoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@Tag(name = "Solicitudes de Préstamo", description = "Registro, consulta y resolución de solicitudes de préstamo")
public class SolicitudPrestamoController {

    private final SolicitudPrestamoService solicitudPrestamoService;

    public SolicitudPrestamoController(SolicitudPrestamoService solicitudPrestamoService) {
        this.solicitudPrestamoService = solicitudPrestamoService;
    }

    @PostMapping
    @Operation(summary = "Registrar una solicitud de préstamo", description = "El usuario CLIENTE autenticado "
            + "registra una solicitud para sí mismo (el id se toma del token, no del body). Si el tipo de "
            + "préstamo tiene validación automática habilitada, la solicitud se evalúa de inmediato contra el "
            + "Stored Procedure y puede quedar aprobada, rechazada o en revisión manual en la misma respuesta; "
            + "si no, queda en PENDIENTE_REVISION.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Solicitud registrada (y posiblemente ya resuelta "
                    + "por evaluación automática)"),
            @ApiResponse(responseCode = "400", description = "Monto o plazo fuera del rango permitido para el "
                    + "tipo de préstamo, o tipo de préstamo inactivo",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene rol CLIENTE"),
            @ApiResponse(responseCode = "404", description = "El tipo de préstamo indicado no existe",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<SolicitudPrestamoResponse> registrar(@Valid @RequestBody RegistrarSolicitudRequest request,
            @AuthenticationPrincipal UsuarioAutenticado principal) {
        SolicitudPrestamo solicitud = solicitudPrestamoService.registrarSolicitud(principal.getId(),
                request.tipoPrestamoId(), request.monto(), request.plazoMeses());

        return ResponseEntity.status(HttpStatus.CREATED).body(SolicitudPrestamoResponse.from(solicitud));
    }

    @GetMapping("/por-estado")
    @Operation(summary = "Listar solicitudes por estado", description = "Consulta paginada de solicitudes, "
            + "opcionalmente filtrada por estado. Solo accesible para ANALISTA.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene rol ANALISTA")
    })
    public ResponseEntity<PaginaResponse<SolicitudPrestamoResponse>> listarPorEstado(
            @RequestParam(required = false)
            @Parameter(description = "Estado por el cual filtrar; si se omite, trae todas las solicitudes")
            EstadoSolicitud estado,
            @RequestParam(defaultValue = "0") @Parameter(description = "Número de página, empieza en 0") int pagina,
            @RequestParam(defaultValue = "20") @Parameter(description = "Cantidad de elementos por página")
            int tamano) {
        PaginaResponse<SolicitudPrestamoResponse> respuesta = solicitudPrestamoService.listarPorEstado(estado,
                pagina, tamano);

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/por-fecha")
    @Operation(summary = "Listar solicitudes por rango de fechas", description = "Consulta paginada de "
            + "solicitudes registradas entre fechaDesde y fechaHasta (ambas obligatorias, ninguna puede ser "
            + "posterior a hoy, y fechaDesde no puede ser posterior a fechaHasta). Solo accesible para ANALISTA.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente"),
            @ApiResponse(responseCode = "400", description = "Alguna fecha es nula, futura, o fechaDesde es "
                    + "posterior a fechaHasta",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene rol ANALISTA")
    })
    public ResponseEntity<PaginaResponse<SolicitudPrestamoResponse>> listarPorFecha(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "Fecha inicial del rango (inclusive), formato ISO yyyy-MM-dd")
            LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "Fecha final del rango (inclusive), formato ISO yyyy-MM-dd")
            LocalDate fechaHasta,
            @RequestParam(defaultValue = "0") @Parameter(description = "Número de página, empieza en 0") int pagina,
            @RequestParam(defaultValue = "20") @Parameter(description = "Cantidad de elementos por página")
            int tamano) {
        PaginaResponse<SolicitudPrestamoResponse> respuesta = solicitudPrestamoService.listarPorFecha(fechaDesde,
                fechaHasta, pagina, tamano);

        return ResponseEntity.ok(respuesta);
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Aprobar o rechazar una solicitud manualmente", description = "El ANALISTA autenticado "
            + "resuelve una solicitud que quedó en PENDIENTE_REVISION o REVISION_MANUAL (el id del analista se "
            + "toma del token, no del body). Al aprobar se genera el préstamo y el plan de pagos, y se notifica "
            + "por correo de forma asíncrona; al rechazar solo se notifica.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitud resuelta exitosamente"),
            @ApiResponse(responseCode = "400", description = "Estado destino inválido, o la solicitud ya fue "
                    + "resuelta previamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene rol ANALISTA"),
            @ApiResponse(responseCode = "404", description = "La solicitud indicada no existe",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<SolicitudPrestamoResponse> actualizarEstado(@PathVariable Long id,
            @Valid @RequestBody ActualizarEstadoSolicitudRequest request,
            @AuthenticationPrincipal UsuarioAutenticado principal) {
        SolicitudPrestamo solicitud = solicitudPrestamoService.actualizarEstadoManual(id, request.nuevoEstado(),
                principal.getId());

        return ResponseEntity.ok(SolicitudPrestamoResponse.from(solicitud));
    }
}
