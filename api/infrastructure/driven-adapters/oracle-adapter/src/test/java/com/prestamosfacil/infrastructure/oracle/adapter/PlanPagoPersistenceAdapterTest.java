package com.prestamosfacil.infrastructure.oracle.adapter;

import com.prestamosfacil.infrastructure.oracle.entity.PlanPagoCuotaEntity;
import com.prestamosfacil.infrastructure.oracle.entity.PrestamoEntity;
import com.prestamosfacil.infrastructure.oracle.mapper.PlanPagoCuotaEntityMapper;
import com.prestamosfacil.infrastructure.oracle.mocks.PrestamoFacilMocks;
import com.prestamosfacil.infrastructure.oracle.repository.IPlanPagoCuotaRepository;
import com.prestamosfacil.infrastructure.oracle.repository.IPrestamoRepository;
import com.prestamosfacil.model.PlanPagoCuota;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlanPagoPersistenceAdapterTest {

    @Mock
    private IPlanPagoCuotaRepository planPagoCuotaRepository;

    @Mock
    private IPrestamoRepository prestamoRepository;

    @Mock
    private PlanPagoCuotaEntityMapper planPagoCuotaEntityMapper;

    @InjectMocks
    private PlanPagoPersistenceAdapter planPagoPersistenceAdapter;

    @Test
    void guardarPlanResuelveLaReferenciaDelPrestamoParaCadaCuota() {
        PlanPagoCuota cuota = PrestamoFacilMocks.getMockPlanPagoCuota();
        PlanPagoCuotaEntity entitySinGuardar = PrestamoFacilMocks.getMockPlanPagoCuotaEntity();
        PrestamoEntity prestamoEntity = PrestamoFacilMocks.getMockPrestamoEntity();
        PlanPagoCuotaEntity entityGuardada = PrestamoFacilMocks.getMockPlanPagoCuotaEntity();

        when(planPagoCuotaEntityMapper.toEntity(cuota)).thenReturn(entitySinGuardar);
        when(prestamoRepository.getReferenceById(500L)).thenReturn(prestamoEntity);
        when(planPagoCuotaRepository.saveAll(List.of(entitySinGuardar))).thenReturn(List.of(entityGuardada));
        when(planPagoCuotaEntityMapper.toDomain(entityGuardada)).thenReturn(cuota);

        List<PlanPagoCuota> resultado = planPagoPersistenceAdapter.guardarPlan(List.of(cuota));

        assertEquals(List.of(cuota), resultado);
        assertEquals(prestamoEntity, entitySinGuardar.getPrestamo());
    }

    @Test
    void buscarPorPrestamoIdMapeaLasCuotasDelRepositorio() {
        PlanPagoCuotaEntity entity = PrestamoFacilMocks.getMockPlanPagoCuotaEntity();
        PlanPagoCuota dominio = PrestamoFacilMocks.getMockPlanPagoCuota();
        when(planPagoCuotaRepository.findByPrestamoId(500L)).thenReturn(List.of(entity));
        when(planPagoCuotaEntityMapper.toDomain(entity)).thenReturn(dominio);

        List<PlanPagoCuota> resultado = planPagoPersistenceAdapter.buscarPorPrestamoId(500L);

        assertEquals(List.of(dominio), resultado);
    }
}
