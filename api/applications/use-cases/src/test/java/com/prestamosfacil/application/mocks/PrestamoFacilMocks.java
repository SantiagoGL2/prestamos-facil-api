package com.prestamosfacil.application.mocks;

import com.prestamosfacil.enums.EstadoSolicitud;
import com.prestamosfacil.enums.RolUsuario;
import com.prestamosfacil.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PrestamoFacilMocks {

    public static Usuario getMockUsuarioAnalista(){
        return new Usuario(5L, "Ana", "Torres", "ana@mail.com",
                new TipoDocumento(1L, "CC", "Cedula", true), "999", BigDecimal.valueOf(3_000_000), "hash",
                RolUsuario.ANALISTA, LocalDateTime.now());
    }

    public static Usuario getMockUsuarioCliente(){
        return new Usuario(1L, "Juan", "Perez", "juan@mail.com",
                new TipoDocumento(1L, "CC", "Cedula", true), "123", BigDecimal.valueOf(2_000_000), "hash",
                RolUsuario.CLIENTE, LocalDateTime.now());
    }

    public static Usuario getMockUsuarioAutenticacion(){
        return new Usuario(1L, "Juan", "Perez", "juan@mail.com",
                new TipoDocumento(1L, "CC", "Cedula", true), "123", BigDecimal.valueOf(2_000_000), "hashGuardado",
                RolUsuario.ANALISTA, LocalDateTime.now());
    }

    public static TipoPrestamo getMockTipoPrestamo(){
        return new TipoPrestamo(1L, "Libre inversion", BigDecimal.valueOf(0.24),
                false, BigDecimal.valueOf(1_000_000), BigDecimal.valueOf(50_000_000), 6, 60, true);
    }

    public static TipoPrestamo getMockTipoPrestamoError(){
        return new TipoPrestamo(1L, "Libre inversion", BigDecimal.valueOf(0.24), false,
                BigDecimal.valueOf(1_000_000), BigDecimal.valueOf(50_000_000), 6, 60, false);
    }

    public static SolicitudPrestamo getMockSolicitudPrestamo(){
        return new SolicitudPrestamo(100L, getMockUsuarioCliente(), getMockTipoPrestamo(),
                BigDecimal.valueOf(5_000_000), 12, EstadoSolicitud.PENDIENTE_REVISION, null, LocalDateTime.now(), null);
    }

    public static List<ReportePrestamoAprobado> getMocksReportePrestamoAprobado(){
        return List.of(
                new ReportePrestamoAprobado("Libre inversion", LocalDateTime.now(), 12, BigDecimal.valueOf(1_000_000)),
                new ReportePrestamoAprobado("Vivienda", LocalDateTime.now(), 60, BigDecimal.valueOf(2_500_000)));
    }

    public static TipoPrestamo getMockTipoPrestamoManual(){
        return new TipoPrestamo(1L, "Vivienda", BigDecimal.valueOf(0.18), false,
                BigDecimal.valueOf(1_000_000), BigDecimal.valueOf(50_000_000), 6, 60, true);
    }

    public static TipoPrestamo getMockTipoPrestamoAutomatico(){
        return new TipoPrestamo(2L, "Rotativo", BigDecimal.valueOf(0.24),
                true, BigDecimal.valueOf(1_000_000), BigDecimal.valueOf(50_000_000), 1, 24, true);
    }
}
