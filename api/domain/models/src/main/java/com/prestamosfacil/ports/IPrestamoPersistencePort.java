package com.prestamosfacil.ports;

import com.prestamosfacil.model.Prestamo;
import com.prestamosfacil.model.ReportePrestamoAprobado;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de persistencia para los préstamos ya aprobados. También expone la proyección usada
 * por el reporte global ({@link #listarAprobados()}), que retorna un objeto de lectura propio
 * en vez del modelo de dominio completo, ya que el reporte no necesita todos sus campos.
 */
public interface IPrestamoPersistencePort {

    Prestamo guardar(Prestamo prestamo);

    Optional<Prestamo> buscarPorSolicitudId(Long solicitudId);

    List<Prestamo> listarActivos();

    List<ReportePrestamoAprobado> listarAprobados();
}
