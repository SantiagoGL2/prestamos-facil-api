package com.prestamosfacil.application.adapter;

import com.prestamosfacil.exception.TipoPrestamoNoEncontradoException;
import com.prestamosfacil.model.TipoPrestamo;
import com.prestamosfacil.ports.ITipoPrestamoPersistencePort;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.prestamosfacil.application.mocks.PrestamoFacilMocks.getMockTipoPrestamo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TipoPrestamoUseCaseTest {

    @Mock
    private ITipoPrestamoPersistencePort tipoPrestamoPersistencePort;

    @InjectMocks
    private TipoPrestamoUseCase tipoPrestamoUseCase;

    private final TipoPrestamo tipoPrestamo = getMockTipoPrestamo();

    @Test
    void listarActivosDelegaEnElPersistencePort() {
        List<TipoPrestamo> tipos = List.of(tipoPrestamo);
        when(tipoPrestamoPersistencePort.listarActivos()).thenReturn(tipos);

        List<TipoPrestamo> resultado = tipoPrestamoUseCase.listarActivos();

        assertEquals(tipos, resultado);
    }

    @Test
    void buscarPorIdRetornaElTipoDePrestamoCuandoExiste() {
        when(tipoPrestamoPersistencePort.buscarPorId(1L)).thenReturn(Optional.of(tipoPrestamo));

        TipoPrestamo resultado = tipoPrestamoUseCase.buscarPorId(1L);

        assertEquals(tipoPrestamo, resultado);
    }

    @Test
    void buscarPorIdLanzaExcepcionCuandoNoExiste() {
        when(tipoPrestamoPersistencePort.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThrows(TipoPrestamoNoEncontradoException.class, () -> tipoPrestamoUseCase.buscarPorId(99L));
    }
}
