package com.prestamosfacil.infrastructure.oracle.adapter;

import com.prestamosfacil.infrastructure.oracle.entity.TipoPrestamoEntity;
import com.prestamosfacil.infrastructure.oracle.mapper.TipoPrestamoEntityMapper;
import com.prestamosfacil.infrastructure.oracle.mocks.PrestamoFacilMocks;
import com.prestamosfacil.infrastructure.oracle.repository.ITipoPrestamoRepository;
import com.prestamosfacil.model.TipoPrestamo;

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
class TipoPrestamoPersistenceAdapterTest {

    @Mock
    private ITipoPrestamoRepository tipoPrestamoRepository;

    @Mock
    private TipoPrestamoEntityMapper tipoPrestamoEntityMapper;

    @InjectMocks
    private TipoPrestamoPersistenceAdapter tipoPrestamoPersistenceAdapter;

    @Test
    void buscarPorIdRetornaElTipoPrestamoCuandoExiste() {
        TipoPrestamoEntity entity = PrestamoFacilMocks.getMockTipoPrestamoEntity();
        TipoPrestamo dominio = PrestamoFacilMocks.getMockTipoPrestamo();
        when(tipoPrestamoRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(tipoPrestamoEntityMapper.toDomain(entity)).thenReturn(dominio);

        Optional<TipoPrestamo> resultado = tipoPrestamoPersistenceAdapter.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(dominio, resultado.get());
    }

    @Test
    void buscarPorIdRetornaVacioCuandoNoExiste() {
        when(tipoPrestamoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<TipoPrestamo> resultado = tipoPrestamoPersistenceAdapter.buscarPorId(99L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void listarActivosMapeaLosResultadosDelRepositorio() {
        TipoPrestamoEntity entity = PrestamoFacilMocks.getMockTipoPrestamoEntity();
        TipoPrestamo dominio = PrestamoFacilMocks.getMockTipoPrestamo();
        when(tipoPrestamoRepository.findByActivoTrue()).thenReturn(List.of(entity));
        when(tipoPrestamoEntityMapper.toDomain(entity)).thenReturn(dominio);

        List<TipoPrestamo> resultado = tipoPrestamoPersistenceAdapter.listarActivos();

        assertEquals(List.of(dominio), resultado);
    }
}
