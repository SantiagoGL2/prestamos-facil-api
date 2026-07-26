package com.prestamosfacil.infrastructure.oracle.repository;

import com.prestamosfacil.infrastructure.oracle.entity.TipoPrestamoEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITipoPrestamoRepository extends JpaRepository<TipoPrestamoEntity, Long> {

    List<TipoPrestamoEntity> findByActivoTrue();
}
