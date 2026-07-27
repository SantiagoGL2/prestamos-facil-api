package com.prestamosfacil.dto.response;

import com.prestamosfacil.model.ReporteMontosAprobados;

import java.math.BigDecimal;
import java.util.List;

public record ReporteMontosAprobadosResponse(List<ReportePrestamoAprobadoResponse> prestamos,
                                              BigDecimal montoTotalAprobado) {

    public static ReporteMontosAprobadosResponse from(ReporteMontosAprobados reporte) {
        return new ReporteMontosAprobadosResponse(
                reporte.prestamos().stream().map(ReportePrestamoAprobadoResponse::from).toList(),
                reporte.montoTotalAprobado());
    }
}
