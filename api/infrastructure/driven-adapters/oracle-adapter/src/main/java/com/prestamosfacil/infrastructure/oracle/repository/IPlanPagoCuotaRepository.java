package com.prestamosfacil.infrastructure.oracle.repository;

import com.prestamosfacil.infrastructure.oracle.entity.PlanPagoCuotaEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPlanPagoCuotaRepository extends JpaRepository<PlanPagoCuotaEntity, Long> {

    List<PlanPagoCuotaEntity> findByPrestamoId(Long prestamoId);
}
