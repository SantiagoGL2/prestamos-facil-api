package com.prestamosfacil.application.service;

import com.prestamosfacil.enums.EstadoPrestamo;
import com.prestamosfacil.enums.EstadoSolicitud;
import com.prestamosfacil.model.Prestamo;
import com.prestamosfacil.model.SolicitudPrestamo;
import com.prestamosfacil.model.TipoPrestamo;
import com.prestamosfacil.model.Usuario;
import com.prestamosfacil.ports.INotificacionPublisherPort;
import com.prestamosfacil.ports.IPlanPagoPersistencePort;
import com.prestamosfacil.ports.IPrestamoPersistencePort;
import com.prestamosfacil.ports.ISolicitudPrestamoPersistencePort;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.prestamosfacil.application.mocks.PrestamoFacilMocks.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcesadorAprobacionSolicitudTest {

    @Mock
    private IPrestamoPersistencePort prestamoPersistencePort;

    @Mock
    private IPlanPagoPersistencePort planPagoPersistencePort;

    @Mock
    private ISolicitudPrestamoPersistencePort solicitudPrestamoPersistencePort;

    @Mock
    private INotificacionPublisherPort notificacionPublisherPort;

    @InjectMocks
    private ProcesadorAprobacionSolicitud procesador;

    private final Usuario usuario = getMockUsuarioCliente();

    private final TipoPrestamo tipoPrestamo = getMockTipoPrestamo();

    private final SolicitudPrestamo solicitud = getMockSolicitudPrestamo();

    @Test
    void procesarAprobacionGuardaPrestamoPlanYActualizaLaSolicitud() {
        Prestamo prestamoGuardado = new Prestamo(500L, solicitud.id(), BigDecimal.valueOf(1_000_000),
                BigDecimal.valueOf(0.02), BigDecimal.valueOf(94_559.60), 12, LocalDateTime.now(),
                EstadoPrestamo.APROBADO);
        when(prestamoPersistencePort.guardar(any())).thenReturn(prestamoGuardado);
        when(planPagoPersistencePort.guardarPlan(any())).thenAnswer(invocacion -> invocacion.getArgument(0));
        when(solicitudPrestamoPersistencePort.guardar(any())).thenAnswer(invocacion -> invocacion.getArgument(0));

        SolicitudPrestamo resultado = procesador.procesarAprobacion(solicitud, BigDecimal.valueOf(1_000_000), 5L);

        verify(prestamoPersistencePort).guardar(any());
        verify(planPagoPersistencePort).guardarPlan(any());
        verify(notificacionPublisherPort).publicarSolicitudResuelta(any());

        ArgumentCaptor<SolicitudPrestamo> captor = ArgumentCaptor.forClass(SolicitudPrestamo.class);
        verify(solicitudPrestamoPersistencePort).guardar(captor.capture());
        assertEquals(EstadoSolicitud.APROBADO, captor.getValue().estado());
        assertEquals(EstadoSolicitud.APROBADO, resultado.estado());
    }

    @Test
    void procesarRechazoActualizaLaSolicitudYPublicaNotificacionSinGenerarPrestamo() {
        when(solicitudPrestamoPersistencePort.guardar(any())).thenAnswer(invocacion -> invocacion.getArgument(0));

        SolicitudPrestamo resultado = procesador.procesarRechazo(solicitud, 5L);

        verify(prestamoPersistencePort, never()).guardar(any());
        verify(planPagoPersistencePort, never()).guardarPlan(any());
        verify(notificacionPublisherPort).publicarSolicitudResuelta(any());

        ArgumentCaptor<SolicitudPrestamo> captor = ArgumentCaptor.forClass(SolicitudPrestamo.class);
        verify(solicitudPrestamoPersistencePort).guardar(captor.capture());
        assertEquals(EstadoSolicitud.RECHAZADO, captor.getValue().estado());
        assertEquals(EstadoSolicitud.RECHAZADO, resultado.estado());
    }
}
