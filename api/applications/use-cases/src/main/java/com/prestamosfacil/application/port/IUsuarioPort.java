package com.prestamosfacil.application.port;

import com.prestamosfacil.model.Usuario;

import java.math.BigDecimal;
import java.util.List;

public interface IUsuarioPort {

    Usuario registrarUsuario(String nombres, String apellidos, String email, Long tipoDocumentoId,
                              String numeroDocumento, BigDecimal salarioBase, String passwordPlano);

    Usuario registrarAnalista(String nombres, String apellidos, String email, Long tipoDocumentoId,
                               String numeroDocumento, BigDecimal salarioBase, String passwordPlano);

    Usuario buscarPorId(Long id);

    Usuario buscarPorTipoDocumentoYNumeroDocumento(Long tipoDocumentoId, String numeroDocumento);

    List<Usuario> listarClientes();

    List<Usuario> listarAnalistas();
}
