package com.prestamosfacil.infrastructure.oracle.repository;

import com.prestamosfacil.infrastructure.oracle.entity.SolicitudPrestamoEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ISolicitudPrestamoRepository
        extends JpaRepository<SolicitudPrestamoEntity, Long>, JpaSpecificationExecutor<SolicitudPrestamoEntity> {
}
