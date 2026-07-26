package com.prestamosfacil.controller;

import com.prestamosfacil.dto.response.TipoPrestamoResponse;
import com.prestamosfacil.model.TipoPrestamo;
import com.prestamosfacil.service.TipoPrestamoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tipos-prestamo")
public class TipoPrestamoController {

    private final TipoPrestamoService tipoPrestamoService;

    public TipoPrestamoController(TipoPrestamoService tipoPrestamoService) {
        this.tipoPrestamoService = tipoPrestamoService;
    }

    @GetMapping
    public ResponseEntity<List<TipoPrestamoResponse>> listar() {
        List<TipoPrestamoResponse> respuesta = tipoPrestamoService.listarActivos().stream()
                .map(TipoPrestamoResponse::from)
                .toList();

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoPrestamoResponse> obtener(@PathVariable Long id) {
        TipoPrestamo tipoPrestamo = tipoPrestamoService.buscarPorId(id);

        return ResponseEntity.ok(TipoPrestamoResponse.from(tipoPrestamo));
    }
}
