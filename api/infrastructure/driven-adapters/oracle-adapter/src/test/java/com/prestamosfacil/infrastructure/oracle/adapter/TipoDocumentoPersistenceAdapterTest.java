package com.prestamosfacil.infrastructure.oracle.adapter;

import com.prestamosfacil.infrastructure.oracle.entity.TipoDocumentoEntity;
import com.prestamosfacil.infrastructure.oracle.mapper.TipoDocumentoEntityMapper;
import com.prestamosfacil.infrastructure.oracle.mocks.PrestamoFacilMocks;
import com.prestamosfacil.infrastructure.oracle.repository.ITipoDocumentoRepository;
import com.prestamosfacil.model.TipoDocumento;

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
class TipoDocumentoPersistenceAdapterTest {

    @Mock
    private ITipoDocumentoRepository tipoDocumentoRepository;

    @Mock
    private TipoDocumentoEntityMapper tipoDocumentoEntityMapper;

    @InjectMocks
    private TipoDocumentoPersistenceAdapter tipoDocumentoPersistenceAdapter;

    @Test
    void buscarPorIdRetornaElTipoDocumentoCuandoExiste() {
        TipoDocumentoEntity entity = PrestamoFacilMocks.getMockTipoDocumentoEntity();
        TipoDocumento dominio = PrestamoFacilMocks.getMockTipoDocumento();
        when(tipoDocumentoRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(tipoDocumentoEntityMapper.toDomain(entity)).thenReturn(dominio);

        Optional<TipoDocumento> resultado = tipoDocumentoPersistenceAdapter.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(dominio, resultado.get());
    }

    @Test
    void buscarPorIdRetornaVacioCuandoNoExiste() {
        when(tipoDocumentoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<TipoDocumento> resultado = tipoDocumentoPersistenceAdapter.buscarPorId(99L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void listarActivosMapeaLosResultadosDelRepositorio() {
        TipoDocumentoEntity entity = PrestamoFacilMocks.getMockTipoDocumentoEntity();
        TipoDocumento dominio = PrestamoFacilMocks.getMockTipoDocumento();
        when(tipoDocumentoRepository.findByActivoTrue()).thenReturn(List.of(entity));
        when(tipoDocumentoEntityMapper.toDomain(entity)).thenReturn(dominio);

        List<TipoDocumento> resultado = tipoDocumentoPersistenceAdapter.listarActivos();

        assertEquals(List.of(dominio), resultado);
    }
}
