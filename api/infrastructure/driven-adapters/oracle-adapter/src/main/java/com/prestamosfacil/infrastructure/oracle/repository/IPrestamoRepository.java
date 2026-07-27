package com.prestamosfacil.infrastructure.oracle.repository;

import com.prestamosfacil.enums.EstadoPrestamo;
import com.prestamosfacil.infrastructure.oracle.entity.PrestamoEntity;
import com.prestamosfacil.model.ReportePrestamoAprobado;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IPrestamoRepository extends JpaRepository<PrestamoEntity, Long> {

    Optional<PrestamoEntity> findBySolicitudId(Long solicitudId);

    List<PrestamoEntity> findByEstado(EstadoPrestamo estado);

    @Query("SELECT new com.prestamosfacil.model.ReportePrestamoAprobado("
            + "tp.nombre, sp.fechaSolicitud, sp.plazoMeses, p.montoAprobado) "
            + "FROM PrestamoEntity p JOIN p.solicitud sp JOIN sp.tipoPrestamo tp")
    List<ReportePrestamoAprobado> listarReporteAprobados();
}
