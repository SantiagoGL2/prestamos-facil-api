package com.prestamosfacil.mocks;

import com.prestamosfacil.enums.EstadoSolicitud;
import com.prestamosfacil.enums.RolUsuario;
import com.prestamosfacil.model.SolicitudPrestamo;
import com.prestamosfacil.model.TipoDocumento;
import com.prestamosfacil.model.TipoPrestamo;
import com.prestamosfacil.model.Usuario;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public static SolicitudPrestamo getMockSolicitudPrestamo(){
        return new SolicitudPrestamo(100L, getMockUsuarioCliente(), getMockTipoPrestamo(),
                BigDecimal.valueOf(5_000_000), 12, EstadoSolicitud.PENDIENTE_REVISION, null, LocalDateTime.now(), null);
    }


}
