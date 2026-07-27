package com.prestamosfacil.ports;

import com.prestamosfacil.model.ReporteSolicitadoEvento;

public interface IReportePublisherPort {

    void publicarReporteParaEnvio(ReporteSolicitadoEvento evento);
}
