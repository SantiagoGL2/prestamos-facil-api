package com.prestamosfacil.controller;

import com.prestamosfacil.dto.response.TipoDocumentoResponse;
import com.prestamosfacil.service.TipoDocumentoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tipos-documento")
@Tag(name = "Tipos de Documento", description = "Catálogo de tipos de documento de identidad (cacheado en memoria)")
public class TipoDocumentoController {

    private final TipoDocumentoService tipoDocumentoService;

    public TipoDocumentoController(TipoDocumentoService tipoDocumentoService) {
        this.tipoDocumentoService = tipoDocumentoService;
    }

    @GetMapping
    @SecurityRequirements
    @Operation(summary = "Listar tipos de documento activos", description = "Catálogo público, no requiere "
            + "autenticación. El resultado se sirve desde cache.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente")
    })
    public ResponseEntity<List<TipoDocumentoResponse>> listar() {
        List<TipoDocumentoResponse> respuesta = tipoDocumentoService.listarActivos().stream()
                .map(TipoDocumentoResponse::from)
                .toList();

        return ResponseEntity.ok(respuesta);
    }
}
