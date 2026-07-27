package com.prestamosfacil.ports;

import com.prestamosfacil.model.Prestamo;
import com.prestamosfacil.model.ReportePrestamoAprobado;

import java.util.List;
import java.util.Optional;

public interface IPrestamoPersistencePort {

    Prestamo guardar(Prestamo prestamo);

    Optional<Prestamo> buscarPorSolicitudId(Long solicitudId);

    List<Prestamo> listarActivos();

    List<ReportePrestamoAprobado> listarAprobados();
}
