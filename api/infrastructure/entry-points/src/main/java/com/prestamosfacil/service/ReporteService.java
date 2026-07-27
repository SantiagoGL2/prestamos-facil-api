package com.prestamosfacil.service;

import com.prestamosfacil.application.port.IReportePort;
import com.prestamosfacil.dto.response.ReporteMontosAprobadosResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReporteService {

    private final IReportePort reportePort;

    public ReporteService(IReportePort reportePort) {
        this.reportePort = reportePort;
    }

    @Transactional(readOnly = true)
    public ReporteMontosAprobadosResponse generarReporte() {
        return ReporteMontosAprobadosResponse.from(reportePort.generarReporteMontosAprobados());
    }

    @Transactional(readOnly = true)
    public void enviarPorCorreoAAnalistas() {
        reportePort.enviarReportePorCorreoAAnalistas();
    }
}
