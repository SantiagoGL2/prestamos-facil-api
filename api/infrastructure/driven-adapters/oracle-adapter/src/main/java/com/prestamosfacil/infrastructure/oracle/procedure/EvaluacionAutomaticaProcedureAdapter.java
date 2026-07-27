package com.prestamosfacil.infrastructure.oracle.procedure;

import com.prestamosfacil.model.ResultadoEvaluacionAutomatica;
import com.prestamosfacil.ports.IEvaluacionAutomaticaPort;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.Map;

/**
 * Único punto de la aplicación que invoca {@code sp_evaluar_prestamo_automatico}. El contrato
 * del procedure es: recibe {@code p_solicitud_id} (IN) y retorna cinco parámetros OUT —
 * {@code p_estado_resultante} ({@code VARCHAR2}: "APROBADO"/"RECHAZADO"/"REVISION_MANUAL") y
 * cuatro montos de apoyo ({@code p_capacidad_maxima}, {@code p_deuda_actual},
 * {@code p_capacidad_disponible}, {@code p_cuota_nueva}), todos {@code NUMBER}. La lógica de
 * negocio que decide el estado vive enteramente en PL/SQL; esta clase solo mapea el resultado
 * a {@link ResultadoEvaluacionAutomatica}, no reinterpreta ni valida los números.
 */
@Component
public class EvaluacionAutomaticaProcedureAdapter implements IEvaluacionAutomaticaPort {

    private static final String P_SOLICITUD_ID = "p_solicitud_id";
    private static final String P_ESTADO_RESULTANTE = "p_estado_resultante";
    private static final String P_CAPACIDAD_MAXIMA = "p_capacidad_maxima";
    private static final String P_DEUDA_ACTUAL = "p_deuda_actual";
    private static final String P_CAPACIDAD_DISPONIBLE = "p_capacidad_disponible";
    private static final String P_CUOTA_NUEVA = "p_cuota_nueva";

    private final SimpleJdbcCall simpleJdbcCall;

    public EvaluacionAutomaticaProcedureAdapter(DataSource dataSource) {
        this.simpleJdbcCall = new SimpleJdbcCall(dataSource)
                .withProcedureName("sp_evaluar_prestamo_automatico")
                .declareParameters(
                        new SqlParameter(P_SOLICITUD_ID, Types.NUMERIC),
                        new SqlOutParameter(P_ESTADO_RESULTANTE, Types.VARCHAR),
                        new SqlOutParameter(P_CAPACIDAD_MAXIMA, Types.NUMERIC),
                        new SqlOutParameter(P_DEUDA_ACTUAL, Types.NUMERIC),
                        new SqlOutParameter(P_CAPACIDAD_DISPONIBLE, Types.NUMERIC),
                        new SqlOutParameter(P_CUOTA_NUEVA, Types.NUMERIC));
    }

    @Override
    public ResultadoEvaluacionAutomatica evaluar(Long solicitudId) {
        Map<String, Object> resultado = simpleJdbcCall.execute(
                new MapSqlParameterSource(P_SOLICITUD_ID, solicitudId));

        return new ResultadoEvaluacionAutomatica(
                (String) resultado.get(P_ESTADO_RESULTANTE),
                (BigDecimal) resultado.get(P_CAPACIDAD_MAXIMA),
                (BigDecimal) resultado.get(P_DEUDA_ACTUAL),
                (BigDecimal) resultado.get(P_CAPACIDAD_DISPONIBLE),
                (BigDecimal) resultado.get(P_CUOTA_NUEVA));
    }
}
