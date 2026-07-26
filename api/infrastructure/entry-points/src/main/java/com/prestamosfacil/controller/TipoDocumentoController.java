package com.prestamosfacil.controller;

import com.prestamosfacil.dto.response.TipoDocumentoResponse;
import com.prestamosfacil.service.TipoDocumentoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tipos-documento")
public class TipoDocumentoController {

    private final TipoDocumentoService tipoDocumentoService;

    public TipoDocumentoController(TipoDocumentoService tipoDocumentoService) {
        this.tipoDocumentoService = tipoDocumentoService;
    }

    @GetMapping
    public ResponseEntity<List<TipoDocumentoResponse>> listar() {
        List<TipoDocumentoResponse> respuesta = tipoDocumentoService.listarActivos().stream()
                .map(TipoDocumentoResponse::from)
                .toList();

        return ResponseEntity.ok(respuesta);
    }
}
