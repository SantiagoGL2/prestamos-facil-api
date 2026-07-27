package com.prestamosfacil.infrastructure.oracle.mocks;

import com.prestamosfacil.enums.EstadoNotificacion;
import com.prestamosfacil.enums.EstadoPrestamo;
import com.prestamosfacil.enums.EstadoSolicitud;
import com.prestamosfacil.enums.RolUsuario;
import com.prestamosfacil.infrastructure.oracle.entity.NotificacionEntity;
import com.prestamosfacil.infrastructure.oracle.entity.PlanPagoCuotaEntity;
import com.prestamosfacil.infrastructure.oracle.entity.PrestamoEntity;
import com.prestamosfacil.infrastructure.oracle.entity.SolicitudPrestamoEntity;
import com.prestamosfacil.infrastructure.oracle.entity.TipoDocumentoEntity;
import com.prestamosfacil.infrastructure.oracle.entity.TipoPrestamoEntity;
import com.prestamosfacil.infrastructure.oracle.entity.UsuarioEntity;
import com.prestamosfacil.model.Notificacion;
import com.prestamosfacil.model.PlanPagoCuota;
import com.prestamosfacil.model.Prestamo;
import com.prestamosfacil.model.ReportePrestamoAprobado;
import com.prestamosfacil.model.SolicitudPrestamo;
import com.prestamosfacil.model.TipoDocumento;
import com.prestamosfacil.model.TipoPrestamo;
import com.prestamosfacil.model.Usuario;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PrestamoFacilMocks {

    public static TipoDocumento getMockTipoDocumento() {
        return new TipoDocumento(1L, "CC", "Cedula", true);
    }

    public static TipoDocumentoEntity getMockTipoDocumentoEntity() {
        TipoDocumentoEntity entity = new TipoDocumentoEntity("CC", "Cedula", true);
        entity.setId(1L);
        return entity;
    }

    public static TipoPrestamo getMockTipoPrestamo() {
        return new TipoPrestamo(1L, "Libre inversion", BigDecimal.valueOf(0.24), false,
                BigDecimal.valueOf(1_000_000), BigDecimal.valueOf(50_000_000), 6, 60, true);
    }

    public static TipoPrestamoEntity getMockTipoPrestamoEntity() {
        TipoPrestamoEntity entity = new TipoPrestamoEntity("Libre inversion", BigDecimal.valueOf(0.24), false,
                BigDecimal.valueOf(1_000_000), BigDecimal.valueOf(50_000_000), 6, 60, true);
        entity.setId(1L);
        return entity;
    }

    public static Usuario getMockUsuarioCliente() {
        return new Usuario(1L, "Juan", "Perez", "juan@mail.com", getMockTipoDocumento(), "123",
                BigDecimal.valueOf(2_000_000), "hash", RolUsuario.CLIENTE, LocalDateTime.now());
    }

    public static UsuarioEntity getMockUsuarioEntity() {
        UsuarioEntity entity = new UsuarioEntity("Juan", "Perez", "juan@mail.com", getMockTipoDocumentoEntity(),
                "123", BigDecimal.valueOf(2_000_000), "hash", RolUsuario.CLIENTE, LocalDateTime.now());
        entity.setId(1L);
        return entity;
    }

    public static SolicitudPrestamoEntity getMockSolicitudPrestamoEntity() {
        SolicitudPrestamoEntity entity = new SolicitudPrestamoEntity(getMockUsuarioEntity(),
                getMockTipoPrestamoEntity(), BigDecimal.valueOf(5_000_000), 12, EstadoSolicitud.PENDIENTE_REVISION,
                null, LocalDateTime.now(), null);
        entity.setId(100L);
        return entity;
    }

    public static PrestamoEntity getMockPrestamoEntity() {
        PrestamoEntity entity = new PrestamoEntity(getMockSolicitudPrestamoEntity(), BigDecimal.valueOf(5_000_000),
                BigDecimal.valueOf(0.02), BigDecimal.valueOf(474_853.85), 12, LocalDateTime.now(),
                EstadoPrestamo.APROBADO);
        entity.setId(500L);
        return entity;
    }

    public static PlanPagoCuotaEntity getMockPlanPagoCuotaEntity() {
        PlanPagoCuotaEntity entity = new PlanPagoCuotaEntity(getMockPrestamoEntity(), 1, BigDecimal.valueOf(474_853.85),
                BigDecimal.valueOf(100_000), BigDecimal.valueOf(374_853.85), BigDecimal.valueOf(4_625_146.15));
        entity.setId(900L);
        return entity;
    }

    public static NotificacionEntity getMockNotificacionEntity() {
        NotificacionEntity entity = new NotificacionEntity(getMockUsuarioEntity(), getMockSolicitudPrestamoEntity(),
                "EMAIL_APROBACION", "Correo enviado", EstadoNotificacion.ENVIADA, LocalDateTime.now());
        entity.setId(700L);
        return entity;
    }

    public static SolicitudPrestamo getMockSolicitudPrestamo() {
        return new SolicitudPrestamo(100L, getMockUsuarioCliente(), getMockTipoPrestamo(),
                BigDecimal.valueOf(5_000_000), 12, EstadoSolicitud.PENDIENTE_REVISION, null, LocalDateTime.now(),
                null);
    }

    public static Prestamo getMockPrestamo() {
        return new Prestamo(500L, 100L, BigDecimal.valueOf(5_000_000), BigDecimal.valueOf(0.02),
                BigDecimal.valueOf(474_853.85), 12, LocalDateTime.now(), EstadoPrestamo.APROBADO);
    }

    public static PlanPagoCuota getMockPlanPagoCuota() {
        return new PlanPagoCuota(900L, 500L, 1, BigDecimal.valueOf(474_853.85), BigDecimal.valueOf(100_000),
                BigDecimal.valueOf(374_853.85), BigDecimal.valueOf(4_625_146.15));
    }

    public static Notificacion getMockNotificacion() {
        return new Notificacion(700L, 1L, 100L, "EMAIL_APROBACION", "Correo enviado", EstadoNotificacion.ENVIADA,
                LocalDateTime.now());
    }

    public static ReportePrestamoAprobado getMockReportePrestamoAprobado() {
        return new ReportePrestamoAprobado("Libre inversion", LocalDateTime.now(), 12, BigDecimal.valueOf(5_000_000));
    }
}
