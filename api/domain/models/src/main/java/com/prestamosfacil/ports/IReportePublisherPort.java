package com.prestamosfacil.ports;

import com.prestamosfacil.model.ReporteSolicitadoEvento;

/**
 * Puerto de publicación para el envío asíncrono del reporte de préstamos aprobados. Se publica
 * un evento por cada destinatario (un analista), no uno solo con la lista completa de
 * destinatarios — así cada envío se procesa y falla de forma independiente.
 */
public interface IReportePublisherPort {

    void publicarReporteParaEnvio(ReporteSolicitadoEvento evento);
}
