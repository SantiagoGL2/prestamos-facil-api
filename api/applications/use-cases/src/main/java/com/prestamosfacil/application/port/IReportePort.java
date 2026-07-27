package com.prestamosfacil.application.port;

import com.prestamosfacil.model.ReporteMontosAprobados;

public interface IReportePort {

    ReporteMontosAprobados generarReporteMontosAprobados();

    void enviarReportePorCorreoAAnalistas();
}
