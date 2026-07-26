package com.prestamosfacil.infrastructure.oracle.repository;

import com.prestamosfacil.infrastructure.oracle.entity.UsuarioEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    Optional<UsuarioEntity> findByEmail(String email);

    Optional<UsuarioEntity> findByTipoDocumentoIdAndNumeroDocumento(Long tipoDocumentoId, String numeroDocumento);

    boolean existsByEmail(String email);

    boolean existsByTipoDocumentoIdAndNumeroDocumento(Long tipoDocumentoId, String numeroDocumento);
}
