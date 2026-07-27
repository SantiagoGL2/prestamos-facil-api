package com.prestamosfacil.infrastructure.oracle.repository;

import com.prestamosfacil.enums.EstadoPrestamo;
import com.prestamosfacil.infrastructure.oracle.entity.PrestamoEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IPrestamoRepository extends JpaRepository<PrestamoEntity, Long> {

    Optional<PrestamoEntity> findBySolicitudId(Long solicitudId);

    List<PrestamoEntity> findByEstado(EstadoPrestamo estado);
}
