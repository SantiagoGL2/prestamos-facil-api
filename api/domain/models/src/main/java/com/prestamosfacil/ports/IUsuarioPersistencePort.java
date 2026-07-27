package com.prestamosfacil.ports;

import com.prestamosfacil.enums.RolUsuario;
import com.prestamosfacil.model.Usuario;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de persistencia para usuarios, sin distinguir en la firma entre rol CLIENTE y
 * ANALISTA — ambos son el mismo agregado de dominio, la diferencia de rol se filtra en tiempo
 * de consulta (ver {@link #listarPorRol(com.prestamosfacil.enums.RolUsuario)}).
 */
public interface IUsuarioPersistencePort {

    Usuario guardar(Usuario usuario);

    Optional<Usuario> buscarPorId(Long id);

    Optional<Usuario> buscarPorEmail(String email);

    Optional<Usuario> buscarPorTipoDocumentoYNumeroDocumento(Long tipoDocumentoId, String numeroDocumento);

    boolean existeEmail(String email);

    boolean existeTipoDocumentoYNumeroDocumento(Long tipoDocumentoId, String numeroDocumento);

    List<Usuario> listarPorRol(RolUsuario rol);
}
