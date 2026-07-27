package com.prestamosfacil.application.adapter;

import com.prestamosfacil.application.service.ProcesadorAprobacionSolicitud;
import com.prestamosfacil.application.strategy.Aprobar;
import com.prestamosfacil.application.strategy.EvaluacionAutomaticaStrategy;
import com.prestamosfacil.application.strategy.EvaluacionManualStrategy;
import com.prestamosfacil.application.strategy.Rechazar;
import com.prestamosfacil.application.strategy.RequerirRevisionManual;
import com.prestamosfacil.enums.EstadoSolicitud;
import com.prestamosfacil.exception.SolicitudInvalidaException;
import com.prestamosfacil.exception.SolicitudPrestamoNoEncontradaException;
import com.prestamosfacil.exception.TipoPrestamoNoEncontradoException;
import com.prestamosfacil.exception.UsuarioNoEncontradoException;
import com.prestamosfacil.model.SolicitudPrestamo;
import com.prestamosfacil.model.TipoPrestamo;
import com.prestamosfacil.model.Usuario;
import com.prestamosfacil.model.pagination.Paginacion;
import com.prestamosfacil.ports.ISolicitudPrestamoPersistencePort;
import com.prestamosfacil.ports.ITipoPrestamoPersistencePort;
import com.prestamosfacil.ports.IUsuarioPersistencePort;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.prestamosfacil.application.mocks.PrestamoFacilMocks.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolicitudPrestamoUseCaseTest {

    @Mock
    private ISolicitudPrestamoPersistencePort solicitudPrestamoPersistencePort;

    @Mock
    private IUsuarioPersistencePort usuarioPersistencePort;

    @Mock
    private ITipoPrestamoPersistencePort tipoPrestamoPersistencePort;

    @Mock
    private ProcesadorAprobacionSolicitud procesadorAprobacionSolicitud;

    @Mock
    private EvaluacionAutomaticaStrategy evaluacionAutomaticaStrategy;

    @Mock
    private EvaluacionManualStrategy evaluacionManualStrategy;

    @InjectMocks
    private SolicitudPrestamoUseCase solicitudPrestamoUseCase;

    private final Usuario cliente = getMockUsuarioCliente();

    private final Usuario analista = getMockUsuarioAnalista();

    private final TipoPrestamo tipoPrestamoManual = getMockTipoPrestamoManual();

    private final TipoPrestamo tipoPrestamoAutomatico = getMockTipoPrestamoAutomatico();

    @Test
    void registrarSolicitudConUsuarioInexistenteLanzaExcepcion() {
        when(usuarioPersistencePort.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class, () -> solicitudPrestamoUseCase.registrarSolicitud(99L, 1L,
                BigDecimal.valueOf(5_000_000), 12));
    }

    @Test
    void registrarSolicitudConTipoPrestamoInexistenteLanzaExcepcion() {
        when(usuarioPersistencePort.buscarPorId(1L)).thenReturn(Optional.of(cliente));
        when(tipoPrestamoPersistencePort.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThrows(TipoPrestamoNoEncontradoException.class, () -> solicitudPrestamoUseCase.registrarSolicitud(1L,
                99L, BigDecimal.valueOf(5_000_000), 12));
    }

    @Test
    void registrarSolicitudConMontoFueraDeRangoLanzaExcepcion() {
        when(usuarioPersistencePort.buscarPorId(1L)).thenReturn(Optional.of(cliente));
        when(tipoPrestamoPersistencePort.buscarPorId(1L)).thenReturn(Optional.of(tipoPrestamoManual));

        assertThrows(SolicitudInvalidaException.class, () -> solicitudPrestamoUseCase.registrarSolicitud(1L, 1L,
                BigDecimal.valueOf(500_000), 12));
    }

    @Test
    void registrarSolicitudConTipoManualQuedaPendienteRevisionSinLlamarAlProcesador() {
        when(usuarioPersistencePort.buscarPorId(1L)).thenReturn(Optional.of(cliente));
        when(tipoPrestamoPersistencePort.buscarPorId(1L)).thenReturn(Optional.of(tipoPrestamoManual));

        SolicitudPrestamo solicitudGuardada = new SolicitudPrestamo(200L, cliente, tipoPrestamoManual,
                BigDecimal.valueOf(5_000_000), 12, EstadoSolicitud.PENDIENTE_REVISION, null, LocalDateTime.now(),
                null);
        when(solicitudPrestamoPersistencePort.guardar(any())).thenReturn(solicitudGuardada);

        SolicitudPrestamo resultado = solicitudPrestamoUseCase.registrarSolicitud(1L, 1L,
                BigDecimal.valueOf(5_000_000), 12);

        assertEquals(EstadoSolicitud.PENDIENTE_REVISION, resultado.estado());
        verify(evaluacionAutomaticaStrategy, never()).evaluar(any());
        verify(procesadorAprobacionSolicitud, never()).procesarAprobacion(any(), any(), any());
        verify(procesadorAprobacionSolicitud, never()).procesarRechazo(any(), any());
    }

    @Test
    void registrarSolicitudConTipoAutomaticoYEstrategiaApruebaLlamaAlProcesador() {
        when(usuarioPersistencePort.buscarPorId(1L)).thenReturn(Optional.of(cliente));
        when(tipoPrestamoPersistencePort.buscarPorId(2L)).thenReturn(Optional.of(tipoPrestamoAutomatico));

        SolicitudPrestamo solicitudGuardada = new SolicitudPrestamo(201L, cliente, tipoPrestamoAutomatico,
                BigDecimal.valueOf(5_000_000), 12, EstadoSolicitud.PENDIENTE_REVISION, null, LocalDateTime.now(),
                null);
        when(solicitudPrestamoPersistencePort.guardar(any())).thenReturn(solicitudGuardada);
        when(evaluacionAutomaticaStrategy.evaluar(any())).thenReturn(new Aprobar(BigDecimal.valueOf(5_000_000)));

        solicitudPrestamoUseCase.registrarSolicitud(1L, 2L, BigDecimal.valueOf(5_000_000), 12);

        verify(procesadorAprobacionSolicitud).procesarAprobacion(solicitudGuardada, BigDecimal.valueOf(5_000_000),
                null);
    }

    @Test
    void registrarSolicitudConTipoAutomaticoYEstrategiaRechazaLlamaAlProcesador() {
        when(usuarioPersistencePort.buscarPorId(1L)).thenReturn(Optional.of(cliente));
        when(tipoPrestamoPersistencePort.buscarPorId(2L)).thenReturn(Optional.of(tipoPrestamoAutomatico));

        SolicitudPrestamo solicitudGuardada = new SolicitudPrestamo(202L, cliente, tipoPrestamoAutomatico,
                BigDecimal.valueOf(5_000_000), 12, EstadoSolicitud.PENDIENTE_REVISION, null, LocalDateTime.now(),
                null);
        when(solicitudPrestamoPersistencePort.guardar(any())).thenReturn(solicitudGuardada);
        when(evaluacionAutomaticaStrategy.evaluar(any())).thenReturn(new Rechazar("Cuota supera la capacidad"));

        solicitudPrestamoUseCase.registrarSolicitud(1L, 2L, BigDecimal.valueOf(5_000_000), 12);

        verify(procesadorAprobacionSolicitud).procesarRechazo(solicitudGuardada, null);
    }

    @Test
    void registrarSolicitudConTipoAutomaticoYRevisionManualGuardaEseEstado() {
        when(usuarioPersistencePort.buscarPorId(1L)).thenReturn(Optional.of(cliente));
        when(tipoPrestamoPersistencePort.buscarPorId(2L)).thenReturn(Optional.of(tipoPrestamoAutomatico));

        SolicitudPrestamo solicitudGuardada = new SolicitudPrestamo(203L, cliente, tipoPrestamoAutomatico,
                BigDecimal.valueOf(5_000_000), 12, EstadoSolicitud.PENDIENTE_REVISION, null, LocalDateTime.now(),
                null);
        when(solicitudPrestamoPersistencePort.guardar(any())).thenAnswer(invocacion -> invocacion.getArgument(0));
        when(evaluacionAutomaticaStrategy.evaluar(any())).thenReturn(new RequerirRevisionManual());

        SolicitudPrestamo resultado = solicitudPrestamoUseCase.registrarSolicitud(1L, 2L,
                BigDecimal.valueOf(5_000_000), 12);

        assertEquals(EstadoSolicitud.REVISION_MANUAL, resultado.estado());
        verify(solicitudPrestamoPersistencePort, times(2)).guardar(any());
        verify(procesadorAprobacionSolicitud, never()).procesarAprobacion(any(), any(), any());
        verify(procesadorAprobacionSolicitud, never()).procesarRechazo(any(), any());
    }

    @Test
    void actualizarEstadoManualConSolicitudInexistenteLanzaExcepcion() {
        when(solicitudPrestamoPersistencePort.buscarPorId(999L)).thenReturn(Optional.empty());

        assertThrows(SolicitudPrestamoNoEncontradaException.class,
                () -> solicitudPrestamoUseCase.actualizarEstadoManual(999L, EstadoSolicitud.APROBADO, 5L));
    }

    @Test
    void actualizarEstadoManualConEstadoDestinoInvalidoLanzaExcepcion() {
        SolicitudPrestamo solicitud = new SolicitudPrestamo(300L, cliente, tipoPrestamoManual,
                BigDecimal.valueOf(5_000_000), 12, EstadoSolicitud.PENDIENTE_REVISION, null, LocalDateTime.now(),
                null);
        when(solicitudPrestamoPersistencePort.buscarPorId(300L)).thenReturn(Optional.of(solicitud));

        assertThrows(SolicitudInvalidaException.class, () -> solicitudPrestamoUseCase.actualizarEstadoManual(300L,
                EstadoSolicitud.REVISION_MANUAL, 5L));
    }

    @Test
    void actualizarEstadoManualConSolicitudYaResueltaLanzaExcepcion() {
        SolicitudPrestamo solicitud = new SolicitudPrestamo(301L, cliente, tipoPrestamoManual,
                BigDecimal.valueOf(5_000_000), 12, EstadoSolicitud.APROBADO, 5L, LocalDateTime.now(),
                LocalDateTime.now());
        when(solicitudPrestamoPersistencePort.buscarPorId(301L)).thenReturn(Optional.of(solicitud));

        assertThrows(SolicitudInvalidaException.class, () -> solicitudPrestamoUseCase.actualizarEstadoManual(301L,
                EstadoSolicitud.APROBADO, 5L));
    }

    @Test
    void actualizarEstadoManualConAnalistaInexistenteLanzaExcepcion() {
        SolicitudPrestamo solicitud = new SolicitudPrestamo(302L, cliente, tipoPrestamoManual,
                BigDecimal.valueOf(5_000_000), 12, EstadoSolicitud.PENDIENTE_REVISION, null, LocalDateTime.now(),
                null);
        when(solicitudPrestamoPersistencePort.buscarPorId(302L)).thenReturn(Optional.of(solicitud));
        when(usuarioPersistencePort.buscarPorId(404L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class, () -> solicitudPrestamoUseCase.actualizarEstadoManual(302L,
                EstadoSolicitud.APROBADO, 404L));
    }

    @Test
    void actualizarEstadoManualConUsuarioSinRolAnalistaLanzaExcepcion() {
        SolicitudPrestamo solicitud = new SolicitudPrestamo(303L, cliente, tipoPrestamoManual,
                BigDecimal.valueOf(5_000_000), 12, EstadoSolicitud.PENDIENTE_REVISION, null, LocalDateTime.now(),
                null);
        when(solicitudPrestamoPersistencePort.buscarPorId(303L)).thenReturn(Optional.of(solicitud));
        when(usuarioPersistencePort.buscarPorId(1L)).thenReturn(Optional.of(cliente));

        assertThrows(SolicitudInvalidaException.class, () -> solicitudPrestamoUseCase.actualizarEstadoManual(303L,
                EstadoSolicitud.APROBADO, 1L));
    }

    @Test
    void actualizarEstadoManualAprobandoLlamaAlProcesador() {
        SolicitudPrestamo solicitud = new SolicitudPrestamo(304L, cliente, tipoPrestamoManual,
                BigDecimal.valueOf(5_000_000), 12, EstadoSolicitud.PENDIENTE_REVISION, null, LocalDateTime.now(),
                null);
        when(solicitudPrestamoPersistencePort.buscarPorId(304L)).thenReturn(Optional.of(solicitud));
        when(usuarioPersistencePort.buscarPorId(5L)).thenReturn(Optional.of(analista));

        solicitudPrestamoUseCase.actualizarEstadoManual(304L, EstadoSolicitud.APROBADO, 5L);

        verify(procesadorAprobacionSolicitud).procesarAprobacion(solicitud, solicitud.monto(), 5L);
        verify(procesadorAprobacionSolicitud, never()).procesarRechazo(any(), any());
    }

    @Test
    void actualizarEstadoManualRechazandoLlamaAlProcesador() {
        SolicitudPrestamo solicitud = new SolicitudPrestamo(305L, cliente, tipoPrestamoManual,
                BigDecimal.valueOf(5_000_000), 12, EstadoSolicitud.REVISION_MANUAL, null, LocalDateTime.now(), null);
        when(solicitudPrestamoPersistencePort.buscarPorId(305L)).thenReturn(Optional.of(solicitud));
        when(usuarioPersistencePort.buscarPorId(5L)).thenReturn(Optional.of(analista));

        solicitudPrestamoUseCase.actualizarEstadoManual(305L, EstadoSolicitud.RECHAZADO, 5L);

        verify(procesadorAprobacionSolicitud).procesarRechazo(solicitud, 5L);
        verify(procesadorAprobacionSolicitud, never()).procesarAprobacion(any(), any(), any());
    }

    @Test
    void listarPorFechaConFechaDesdePosteriorAFechaHastaLanzaExcepcion() {
        LocalDate fechaDesde = LocalDate.now();
        LocalDate fechaHasta = fechaDesde.minusDays(5);

        assertThrows(SolicitudInvalidaException.class, () -> solicitudPrestamoUseCase.listarPorFecha(fechaDesde,
                fechaHasta, new Paginacion(0, 20)));
    }
}
