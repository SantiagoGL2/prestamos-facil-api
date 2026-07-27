package com.prestamosfacil.controller;

import com.prestamosfacil.dto.response.TipoPrestamoResponse;
import com.prestamosfacil.exceptionHandler.ErrorResponse;
import com.prestamosfacil.model.TipoPrestamo;
import com.prestamosfacil.service.TipoPrestamoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tipos-prestamo")
@Tag(name = "Tipos de Préstamo", description = "Catálogo de tipos de préstamo (cacheado en memoria)")
public class TipoPrestamoController {

    private final TipoPrestamoService tipoPrestamoService;

    public TipoPrestamoController(TipoPrestamoService tipoPrestamoService) {
        this.tipoPrestamoService = tipoPrestamoService;
    }

    @GetMapping
    @SecurityRequirements
    @Operation(summary = "Listar tipos de préstamo activos", description = "Catálogo público, no requiere "
            + "autenticación. El resultado se sirve desde cache.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente")
    })
    public ResponseEntity<List<TipoPrestamoResponse>> listar() {
        List<TipoPrestamoResponse> respuesta = tipoPrestamoService.listarActivos().stream()
                .map(TipoPrestamoResponse::from)
                .toList();

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}")
    @SecurityRequirements
    @Operation(summary = "Consultar un tipo de préstamo por id", description = "Catálogo público, no requiere "
            + "autenticación.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo de préstamo encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe un tipo de préstamo con ese id",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<TipoPrestamoResponse> obtener(@PathVariable Long id) {
        TipoPrestamo tipoPrestamo = tipoPrestamoService.buscarPorId(id);

        return ResponseEntity.ok(TipoPrestamoResponse.from(tipoPrestamo));
    }
}
