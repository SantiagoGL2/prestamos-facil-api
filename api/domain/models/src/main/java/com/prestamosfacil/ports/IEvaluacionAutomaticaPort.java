package com.prestamosfacil.ports;

import com.prestamosfacil.model.ResultadoEvaluacionAutomatica;

public interface IEvaluacionAutomaticaPort {

    ResultadoEvaluacionAutomatica evaluar(Long solicitudId);
}
