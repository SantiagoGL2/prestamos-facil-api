package com.prestamosfacil.infrastructure.rabbitmq.mocks;

import com.prestamosfacil.model.NotificacionRegistroEvento;
import com.prestamosfacil.model.NotificacionSolicitudEvento;
import com.prestamosfacil.model.PlanPagoCuota;
import com.prestamosfacil.model.ReportePrestamoAprobado;
import com.prestamosfacil.model.ReporteSolicitadoEvento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PrestamoFacilMocks {

    public static PlanPagoCuota getMockPlanPagoCuota() {
        return new PlanPagoCuota(900L, 500L, 1, BigDecimal.valueOf(474_853.85), BigDecimal.valueOf(100_000),
                BigDecimal.valueOf(374_853.85), BigDecimal.valueOf(4_625_146.15));
    }

    public static NotificacionSolicitudEvento getMockNotificacionSolicitudEventoAprobado() {
        return new NotificacionSolicitudEvento(100L, 1L, "Juan Perez", "juan@mail.com", "Libre inversion",
                "APROBADO", BigDecimal.valueOf(5_000_000), BigDecimal.valueOf(474_853.85),
                List.of(getMockPlanPagoCuota()));
    }

    public static NotificacionSolicitudEvento getMockNotificacionSolicitudEventoRechazado() {
        return new NotificacionSolicitudEvento(100L, 1L, "Juan Perez", "juan@mail.com", "Libre inversion",
                "RECHAZADO", null, null, List.of());
    }

    public static NotificacionRegistroEvento getMockNotificacionRegistroEvento() {
        return new NotificacionRegistroEvento(1L, "Juan Perez", "juan@mail.com", "CLIENTE");
    }

    public static ReportePrestamoAprobado getMockReportePrestamoAprobado() {
        return new ReportePrestamoAprobado("Libre inversion", LocalDateTime.now(), 12, BigDecimal.valueOf(5_000_000));
    }

    public static ReporteSolicitadoEvento getMockReporteSolicitadoEvento() {
        return new ReporteSolicitadoEvento("ana@mail.com", "Ana Torres", List.of(getMockReportePrestamoAprobado()),
                BigDecimal.valueOf(5_000_000));
    }
}
