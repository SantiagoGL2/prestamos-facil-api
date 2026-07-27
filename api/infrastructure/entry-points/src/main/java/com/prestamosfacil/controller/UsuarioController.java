package com.prestamosfacil.controller;

import com.prestamosfacil.dto.request.RegistrarUsuarioRequest;
import com.prestamosfacil.dto.response.UsuarioResponse;
import com.prestamosfacil.exceptionHandler.ErrorResponse;
import com.prestamosfacil.service.UsuarioService;
import com.prestamosfacil.model.Usuario;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@Validated
@Tag(name = "Usuarios", description = "Registro y consulta de usuarios con rol CLIENTE")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @SecurityRequirements
    @Operation(summary = "Registrar un cliente", description = "Crea un nuevo usuario con rol CLIENTE. No requiere "
            + "autenticación. Envía un correo de bienvenida de forma asíncrona.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (formato de campos o salario fuera "
                    + "de rango permitido)",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "El tipo de documento indicado no existe",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Ya existe un usuario con ese email o con ese "
                    + "documento",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody RegistrarUsuarioRequest request) {
        Usuario usuario = usuarioService.registrarUsuario(request.nombres(), request.apellidos(), request.email(),
                request.tipoDocumentoId(), request.numeroDocumento(), request.salarioBase(), request.password());

        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponse.from(usuario));
    }

    @GetMapping
    @Operation(summary = "Listar todos los clientes", description = "Retorna todos los usuarios con rol CLIENTE. "
            + "Solo accesible para usuarios con rol ANALISTA.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene rol ANALISTA")
    })
    public ResponseEntity<List<UsuarioResponse>> listarClientes() {
        List<UsuarioResponse> respuesta = usuarioService.listarClientes().stream()
                .map(UsuarioResponse::from)
                .toList();

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar un usuario por id", description = "Solo accesible para usuarios con rol "
            + "ANALISTA.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene rol ANALISTA"),
            @ApiResponse(responseCode = "404", description = "No existe un usuario con ese id",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<UsuarioResponse> obtener(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);

        return ResponseEntity.ok(UsuarioResponse.from(usuario));
    }

    @GetMapping("/tipo-documento/{tipoDocumentoId}/numero-documento/{numeroDocumento}")
    @Operation(summary = "Consultar un usuario por tipo y número de documento", description = "Solo accesible "
            + "para usuarios con rol ANALISTA.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene rol ANALISTA"),
            @ApiResponse(responseCode = "404", description = "No existe un usuario con ese tipo y número de "
                    + "documento",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<UsuarioResponse> obtenerPorTipoDocumentoYNumeroDocumento(@PathVariable @NotNull(message = "El tipo de documento es obligatorio") Long tipoDocumentoId,
            @PathVariable @NotBlank(message = "El numero de documento es obligatorio") String numeroDocumento) {
        Usuario usuario = usuarioService.buscarPorTipoDocumentoYNumeroDocumento(tipoDocumentoId, numeroDocumento);

        return ResponseEntity.ok(UsuarioResponse.from(usuario));
    }
}
