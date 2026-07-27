package com.prestamosfacil.controller;

import com.prestamosfacil.dto.request.RegistrarUsuarioRequest;
import com.prestamosfacil.dto.response.UsuarioResponse;
import com.prestamosfacil.exceptionHandler.ErrorResponse;
import com.prestamosfacil.model.Usuario;
import com.prestamosfacil.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/analistas")
@Tag(name = "Analistas", description = "Registro de usuarios con rol ANALISTA")
public class AnalistaController {

    private final UsuarioService usuarioService;

    public AnalistaController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @Operation(summary = "Registrar un analista", description = "Crea un nuevo usuario con rol ANALISTA. Solo "
            + "puede ejecutarlo otro usuario ya autenticado con rol ANALISTA — no existe un registro público de "
            + "analistas, por eso se requiere sembrar uno inicial vía migración.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Analista registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (formato de campos o salario fuera "
                    + "de rango permitido)",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene rol ANALISTA"),
            @ApiResponse(responseCode = "404", description = "El tipo de documento indicado no existe",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Ya existe un usuario con ese email o con ese "
                    + "documento",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody RegistrarUsuarioRequest request) {
        Usuario usuario = usuarioService.registrarAnalista(request.nombres(), request.apellidos(), request.email(),
                request.tipoDocumentoId(), request.numeroDocumento(), request.salarioBase(), request.password());

        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponse.from(usuario));
    }

    @GetMapping
    @Operation(summary = "Listar todos los analistas", description = "Retorna todos los usuarios con rol "
            + "ANALISTA. Solo accesible para usuarios con rol ANALISTA.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene rol ANALISTA")
    })
    public ResponseEntity<List<UsuarioResponse>> listarAnalistas() {
        List<UsuarioResponse> respuesta = usuarioService.listarAnalistas().stream()
                .map(UsuarioResponse::from)
                .toList();

        return ResponseEntity.ok(respuesta);
    }
}
