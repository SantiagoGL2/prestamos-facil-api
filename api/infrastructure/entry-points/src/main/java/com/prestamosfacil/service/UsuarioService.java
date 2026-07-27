package com.prestamosfacil.service;

import com.prestamosfacil.application.port.IUsuarioPort;
import com.prestamosfacil.model.Usuario;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UsuarioService {

    private final IUsuarioPort usuarioPort;

    public UsuarioService(IUsuarioPort usuarioPort) {
        this.usuarioPort = usuarioPort;
    }

    @Transactional(rollbackFor = Exception.class)
    public Usuario registrarUsuario(String nombres, String apellidos, String email, Long tipoDocumentoId,
                                     String numeroDocumento, BigDecimal salarioBase, String passwordPlano) {
        return usuarioPort.registrarUsuario(nombres, apellidos, email, tipoDocumentoId, numeroDocumento, salarioBase,
                passwordPlano);
    }

    @Transactional(rollbackFor = Exception.class)
    public Usuario registrarAnalista(String nombres, String apellidos, String email, Long tipoDocumentoId,
                                      String numeroDocumento, BigDecimal salarioBase, String passwordPlano) {
        return usuarioPort.registrarAnalista(nombres, apellidos, email, tipoDocumentoId, numeroDocumento,
                salarioBase, passwordPlano);
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioPort.buscarPorId(id);
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorTipoDocumentoYNumeroDocumento(Long tipoDocumentoId, String numeroDocumento) {
        return usuarioPort.buscarPorTipoDocumentoYNumeroDocumento(tipoDocumentoId, numeroDocumento);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarClientes() {
        return usuarioPort.listarClientes();
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarAnalistas() {
        return usuarioPort.listarAnalistas();
    }
}
