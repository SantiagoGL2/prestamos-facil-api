package com.prestamosfacil.infrastructure.oracle.adapter;

import com.prestamosfacil.infrastructure.oracle.entity.PlanPagoCuotaEntity;
import com.prestamosfacil.infrastructure.oracle.mapper.PlanPagoCuotaEntityMapper;
import com.prestamosfacil.infrastructure.oracle.repository.IPlanPagoCuotaRepository;
import com.prestamosfacil.infrastructure.oracle.repository.IPrestamoRepository;
import com.prestamosfacil.model.PlanPagoCuota;
import com.prestamosfacil.ports.IPlanPagoPersistencePort;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlanPagoPersistenceAdapter implements IPlanPagoPersistencePort {

    private final IPlanPagoCuotaRepository planPagoCuotaRepository;
    private final IPrestamoRepository prestamoRepository;
    private final PlanPagoCuotaEntityMapper planPagoCuotaEntityMapper;

    public PlanPagoPersistenceAdapter(IPlanPagoCuotaRepository planPagoCuotaRepository,
                                      IPrestamoRepository prestamoRepository,
                                      PlanPagoCuotaEntityMapper planPagoCuotaEntityMapper) {
        this.planPagoCuotaRepository = planPagoCuotaRepository;
        this.prestamoRepository = prestamoRepository;
        this.planPagoCuotaEntityMapper = planPagoCuotaEntityMapper;
    }

    @Override
    public List<PlanPagoCuota> guardarPlan(List<PlanPagoCuota> cuotas) {
        List<PlanPagoCuotaEntity> entidades = cuotas.stream()
                .map(cuota -> {
                    PlanPagoCuotaEntity entity = planPagoCuotaEntityMapper.toEntity(cuota);
                    entity.setPrestamo(prestamoRepository.getReferenceById(cuota.prestamoId()));

                    return entity;
                })
                .toList();

        return planPagoCuotaRepository.saveAll(entidades).stream()
                .map(planPagoCuotaEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<PlanPagoCuota> buscarPorPrestamoId(Long prestamoId) {
        return planPagoCuotaRepository.findByPrestamoId(prestamoId).stream()
                .map(planPagoCuotaEntityMapper::toDomain)
                .toList();
    }
}
