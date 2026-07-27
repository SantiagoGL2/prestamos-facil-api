package com.prestamosfacil.ports;

import com.prestamosfacil.model.PlanPagoCuota;

import java.util.List;

/**
 * Puerto de persistencia para el plan de pagos (cuotas) generado al aprobar un préstamo.
 * El plan se guarda completo de una sola vez tras el cálculo de amortización — no hay
 * actualizaciones cuota por cuota.
 */
public interface IPlanPagoPersistencePort {

    List<PlanPagoCuota> guardarPlan(List<PlanPagoCuota> cuotas);

    List<PlanPagoCuota> buscarPorPrestamoId(Long prestamoId);
}
