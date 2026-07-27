package com.prestamosfacil.service;

import com.prestamosfacil.application.port.ITipoPrestamoPort;
import com.prestamosfacil.model.TipoPrestamo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TipoPrestamoServiceTest {

    @Mock
    private ITipoPrestamoPort tipoPrestamoPort;

    @InjectMocks
    private TipoPrestamoService tipoPrestamoService;

    private final TipoPrestamo tipoPrestamo = new TipoPrestamo(1L, "Libre inversion", BigDecimal.valueOf(0.24),
            false, BigDecimal.valueOf(1_000_000), BigDecimal.valueOf(50_000_000), 6, 60, true);

    @Test
    void listarActivosDelegaEnElPort() {
        List<TipoPrestamo> tipos = List.of(tipoPrestamo);
        when(tipoPrestamoPort.listarActivos()).thenReturn(tipos);

        List<TipoPrestamo> resultado = tipoPrestamoService.listarActivos();

        assertEquals(tipos, resultado);
    }

    @Test
    void buscarPorIdDelegaEnElPort() {
        when(tipoPrestamoPort.buscarPorId(1L)).thenReturn(tipoPrestamo);

        TipoPrestamo resultado = tipoPrestamoService.buscarPorId(1L);

        assertEquals(tipoPrestamo, resultado);
    }
}
