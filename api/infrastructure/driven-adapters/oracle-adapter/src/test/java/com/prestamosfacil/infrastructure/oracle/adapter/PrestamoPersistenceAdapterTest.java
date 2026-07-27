package com.prestamosfacil.infrastructure.oracle.adapter;

import com.prestamosfacil.enums.EstadoPrestamo;
import com.prestamosfacil.infrastructure.oracle.entity.PrestamoEntity;
import com.prestamosfacil.infrastructure.oracle.entity.SolicitudPrestamoEntity;
import com.prestamosfacil.infrastructure.oracle.mapper.PrestamoEntityMapper;
import com.prestamosfacil.infrastructure.oracle.mocks.PrestamoFacilMocks;
import com.prestamosfacil.infrastructure.oracle.repository.IPrestamoRepository;
import com.prestamosfacil.infrastructure.oracle.repository.ISolicitudPrestamoRepository;
import com.prestamosfacil.model.Prestamo;
import com.prestamosfacil.model.ReportePrestamoAprobado;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PrestamoPersistenceAdapterTest {

    @Mock
    private IPrestamoRepository prestamoRepository;

    @Mock
    private ISolicitudPrestamoRepository solicitudPrestamoRepository;

    @Mock
    private PrestamoEntityMapper prestamoEntityMapper;

    @InjectMocks
    private PrestamoPersistenceAdapter prestamoPersistenceAdapter;

    @Test
    void guardarResuelveLaReferenciaDeLaSolicitudYRetornaElDominio() {
        Prestamo prestamo = PrestamoFacilMocks.getMockPrestamo();
        PrestamoEntity entitySinGuardar = PrestamoFacilMocks.getMockPrestamoEntity();
        SolicitudPrestamoEntity solicitudEntity = PrestamoFacilMocks.getMockSolicitudPrestamoEntity();
        PrestamoEntity entityGuardada = PrestamoFacilMocks.getMockPrestamoEntity();

        when(prestamoEntityMapper.toEntity(prestamo)).thenReturn(entitySinGuardar);
        when(solicitudPrestamoRepository.getReferenceById(100L)).thenReturn(solicitudEntity);
        when(prestamoRepository.save(entitySinGuardar)).thenReturn(entityGuardada);
        when(prestamoEntityMapper.toDomain(entityGuardada)).thenReturn(prestamo);

        Prestamo resultado = prestamoPersistenceAdapter.guardar(prestamo);

        assertEquals(prestamo, resultado);
        assertEquals(solicitudEntity, entitySinGuardar.getSolicitud());
    }

    @Test
    void buscarPorSolicitudIdRetornaVacioCuandoNoExiste() {
        when(prestamoRepository.findBySolicitudId(999L)).thenReturn(Optional.empty());

        Optional<Prestamo> resultado = prestamoPersistenceAdapter.buscarPorSolicitudId(999L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void listarActivosFiltraPorEstadoAprobado() {
        PrestamoEntity entity = PrestamoFacilMocks.getMockPrestamoEntity();
        Prestamo dominio = PrestamoFacilMocks.getMockPrestamo();
        when(prestamoRepository.findByEstado(EstadoPrestamo.APROBADO)).thenReturn(List.of(entity));
        when(prestamoEntityMapper.toDomain(entity)).thenReturn(dominio);

        List<Prestamo> resultado = prestamoPersistenceAdapter.listarActivos();

        assertEquals(List.of(dominio), resultado);
    }

    @Test
    void listarAprobadosDelegaDirectoEnElRepositorioSinMapper() {
        List<ReportePrestamoAprobado> reporte = List.of(PrestamoFacilMocks.getMockReportePrestamoAprobado());
        when(prestamoRepository.listarReporteAprobados()).thenReturn(reporte);

        List<ReportePrestamoAprobado> resultado = prestamoPersistenceAdapter.listarAprobados();

        assertEquals(reporte, resultado);
    }
}
