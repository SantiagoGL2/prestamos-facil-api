package com.prestamosfacil.controller;

import com.prestamosfacil.dto.request.RegistrarUsuarioRequest;
import com.prestamosfacil.dto.response.UsuarioResponse;
import com.prestamosfacil.service.UsuarioService;
import com.prestamosfacil.model.Usuario;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/usuarios")
@Validated
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody RegistrarUsuarioRequest request) {
        Usuario usuario = usuarioService.registrarUsuario(request.nombres(), request.apellidos(), request.email(),
                request.tipoDocumentoId(), request.numeroDocumento(), request.salarioBase(), request.password());

        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponse.from(usuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> obtener(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);

        return ResponseEntity.ok(UsuarioResponse.from(usuario));
    }

    @GetMapping("/tipo-documento/{tipoDocumentoId}/numero-documento/{numeroDocumento}")
    public ResponseEntity<UsuarioResponse> obtenerPorTipoDocumentoYNumeroDocumento(@PathVariable @NotNull(message = "El tipo de documento es obligatorio") Long tipoDocumentoId,
            @PathVariable @NotBlank(message = "El numero de documento es obligatorio") String numeroDocumento) {
        Usuario usuario = usuarioService.buscarPorTipoDocumentoYNumeroDocumento(tipoDocumentoId, numeroDocumento);

        return ResponseEntity.ok(UsuarioResponse.from(usuario));
    }
}
