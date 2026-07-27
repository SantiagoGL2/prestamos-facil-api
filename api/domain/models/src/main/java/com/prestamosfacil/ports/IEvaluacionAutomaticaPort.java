package com.prestamosfacil.ports;

import com.prestamosfacil.model.ResultadoEvaluacionAutomatica;

/**
 * Puerto que abstrae la evaluación automática de una solicitud de préstamo. La decisión de
 * negocio (aprobar, rechazar o derivar a revisión manual) no vive en el dominio ni en los casos
 * de uso: la calcula un Stored Procedure en la base de datos, y quien implemente este puerto
 * solo traduce esa respuesta a un {@link com.prestamosfacil.model.ResultadoEvaluacionAutomatica}.
 */
public interface IEvaluacionAutomaticaPort {

    ResultadoEvaluacionAutomatica evaluar(Long solicitudId);
}
