package com.prestamosfacil.application.service;

import com.prestamosfacil.constant.ReglasNegocioConstantes;
import com.prestamosfacil.enums.EstadoPrestamo;
import com.prestamosfacil.enums.EstadoSolicitud;
import com.prestamosfacil.model.NotificacionSolicitudEvento;
import com.prestamosfacil.model.PlanPagoCuota;
import com.prestamosfacil.model.Prestamo;
import com.prestamosfacil.model.SolicitudPrestamo;
import com.prestamosfacil.ports.INotificacionPublisherPort;
import com.prestamosfacil.ports.IPlanPagoPersistencePort;
import com.prestamosfacil.ports.IPrestamoPersistencePort;
import com.prestamosfacil.ports.ISolicitudPrestamoPersistencePort;
import com.prestamosfacil.utils.CalculadoraAmortizacion;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Concentra la lógica de "qué pasa cuando una solicitud queda aprobada o rechazada": calcular
 * la cuota, generar préstamo y plan de pagos, actualizar el estado de la solicitud, y publicar
 * la notificación. Vive separada de los casos de uso porque exactamente el mismo procesamiento
 * lo dispara tanto la resolución manual de un analista ({@code SolicitudPrestamoUseCase}) como
 * la evaluación automática vía Stored Procedure — duplicarla en ambos flujos habría sido fácil
 * de desincronizar a futuro.
 */
@Service
public class ProcesadorAprobacionSolicitud {

    private final IPrestamoPersistencePort prestamoPersistencePort;
    private final IPlanPagoPersistencePort planPagoPersistencePort;
    private final ISolicitudPrestamoPersistencePort solicitudPrestamoPersistencePort;
    private final INotificacionPublisherPort notificacionPublisherPort;

    public ProcesadorAprobacionSolicitud(IPrestamoPersistencePort prestamoPersistencePort,
                                         IPlanPagoPersistencePort planPagoPersistencePort,
                                         ISolicitudPrestamoPersistencePort solicitudPrestamoPersistencePort,
                                         INotificacionPublisherPort notificacionPublisherPort) {
        this.prestamoPersistencePort = prestamoPersistencePort;
        this.planPagoPersistencePort = planPagoPersistencePort;
        this.solicitudPrestamoPersistencePort = solicitudPrestamoPersistencePort;
        this.notificacionPublisherPort = notificacionPublisherPort;
    }

    public SolicitudPrestamo procesarAprobacion(SolicitudPrestamo solicitud, BigDecimal montoAprobado,
                                                 Long analistaId) {
        BigDecimal tasaAnual = solicitud.tipoPrestamo().tasaInteresAnual();
        BigDecimal cuotaMensual = CalculadoraAmortizacion.calcularCuotaMensual(montoAprobado, tasaAnual,
                solicitud.plazoMeses());
        BigDecimal tasaInteresMensual = tasaAnual.divide(BigDecimal.valueOf(ReglasNegocioConstantes.MESES_POR_ANIO),
                ReglasNegocioConstantes.ESCALA_CALCULO_INTERMEDIO, ReglasNegocioConstantes.MODO_REDONDEO_MONETARIO);

        Prestamo prestamo = new Prestamo(null, solicitud.id(), montoAprobado, tasaInteresMensual, cuotaMensual,
                solicitud.plazoMeses(), LocalDateTime.now(), EstadoPrestamo.APROBADO);
        Prestamo prestamoGuardado = prestamoPersistencePort.guardar(prestamo);

        List<PlanPagoCuota> plan = CalculadoraAmortizacion.generarPlanPagos(prestamoGuardado.id(), montoAprobado,
                tasaAnual, solicitud.plazoMeses());
        List<PlanPagoCuota> planGuardado = planPagoPersistencePort.guardarPlan(plan);

        SolicitudPrestamo solicitudActualizada = solicitudPrestamoPersistencePort.guardar(
                solicitud.resuelta(EstadoSolicitud.APROBADO, analistaId));

        notificacionPublisherPort.publicarSolicitudResuelta(new NotificacionSolicitudEvento(solicitud.id(),
                solicitud.usuario().id(), solicitud.usuario().nombres() + " " + solicitud.usuario().apellidos(),
                solicitud.usuario().email(), solicitud.tipoPrestamo().nombre(), "APROBADO", montoAprobado,
                cuotaMensual, planGuardado));

        return solicitudActualizada;
    }

    public SolicitudPrestamo procesarRechazo(SolicitudPrestamo solicitud, Long analistaId) {
        SolicitudPrestamo solicitudActualizada = solicitudPrestamoPersistencePort.guardar(
                solicitud.resuelta(EstadoSolicitud.RECHAZADO, analistaId));

        notificacionPublisherPort.publicarSolicitudResuelta(new NotificacionSolicitudEvento(solicitud.id(),
                solicitud.usuario().id(), solicitud.usuario().nombres() + " " + solicitud.usuario().apellidos(),
                solicitud.usuario().email(), solicitud.tipoPrestamo().nombre(), "RECHAZADO", null, null, List.of()));

        return solicitudActualizada;
    }
}
