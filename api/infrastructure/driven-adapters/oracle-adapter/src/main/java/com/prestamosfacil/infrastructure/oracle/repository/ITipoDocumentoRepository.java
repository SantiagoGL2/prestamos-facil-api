package com.prestamosfacil.infrastructure.oracle.repository;

import com.prestamosfacil.infrastructure.oracle.entity.TipoDocumentoEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITipoDocumentoRepository extends JpaRepository<TipoDocumentoEntity, Long> {

    List<TipoDocumentoEntity> findByActivoTrue();
}
