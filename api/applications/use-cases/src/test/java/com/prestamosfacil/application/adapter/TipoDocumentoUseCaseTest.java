package com.prestamosfacil.application.adapter;

import com.prestamosfacil.model.TipoDocumento;
import com.prestamosfacil.ports.ITipoDocumentoPersistencePort;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TipoDocumentoUseCaseTest {

    @Mock
    private ITipoDocumentoPersistencePort tipoDocumentoPersistencePort;

    @InjectMocks
    private TipoDocumentoUseCase tipoDocumentoUseCase;

    @Test
    void listarActivosDelegaEnElPersistencePort() {
        List<TipoDocumento> tipos = List.of(
                new TipoDocumento(1L, "CC", "Cedula de ciudadania", true),
                new TipoDocumento(2L, "CE", "Cedula de extranjeria", true));
        when(tipoDocumentoPersistencePort.listarActivos()).thenReturn(tipos);

        List<TipoDocumento> resultado = tipoDocumentoUseCase.listarActivos();

        assertEquals(tipos, resultado);
    }
}
