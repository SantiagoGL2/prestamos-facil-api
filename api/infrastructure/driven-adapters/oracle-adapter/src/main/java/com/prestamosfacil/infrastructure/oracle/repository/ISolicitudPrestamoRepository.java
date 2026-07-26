package com.prestamosfacil.infrastructure.oracle.repository;

import com.prestamosfacil.enums.EstadoSolicitud;
import com.prestamosfacil.infrastructure.oracle.entity.SolicitudPrestamoEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ISolicitudPrestamoRepository extends JpaRepository<SolicitudPrestamoEntity, Long> {

    Page<SolicitudPrestamoEntity> findByEstado(EstadoSolicitud estado, Pageable pageable);

    Page<SolicitudPrestamoEntity> findByFechaSolicitudBetween(LocalDateTime desde, LocalDateTime hasta,
                                                               Pageable pageable);
}
