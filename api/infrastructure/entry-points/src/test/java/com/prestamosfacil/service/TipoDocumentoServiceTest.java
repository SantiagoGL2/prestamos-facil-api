package com.prestamosfacil.service;

import com.prestamosfacil.application.port.ITipoDocumentoPort;
import com.prestamosfacil.model.TipoDocumento;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TipoDocumentoServiceTest {

    @Mock
    private ITipoDocumentoPort tipoDocumentoPort;

    @InjectMocks
    private TipoDocumentoService tipoDocumentoService;

    @Test
    void listarActivosDelegaEnElPort() {
        List<TipoDocumento> tipos = List.of(new TipoDocumento(1L, "CC", "Cedula de ciudadania", true));
        when(tipoDocumentoPort.listarActivos()).thenReturn(tipos);

        List<TipoDocumento> resultado = tipoDocumentoService.listarActivos();

        assertEquals(tipos, resultado);
    }
}
