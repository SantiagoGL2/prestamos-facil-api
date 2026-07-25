package com.prestamosfacil.ports;

import com.prestamosfacil.model.PlanPagoCuota;

import java.util.List;

public interface IPlanPagoPersistencePort {

    List<PlanPagoCuota> guardarPlan(List<PlanPagoCuota> cuotas);

    List<PlanPagoCuota> buscarPorPrestamoId(Long prestamoId);
}
