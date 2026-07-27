package com.prestamosfacil.utils;

import com.prestamosfacil.constant.ReglasNegocioConstantes;
import com.prestamosfacil.model.PlanPagoCuota;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

/**
 * Implementa el sistema de amortización francés (cuota fija, interés decreciente) usado para
 * todo préstamo aprobado, sea por evaluación automática o por aprobación manual de un analista.
 */
public final class CalculadoraAmortizacion {

    private CalculadoraAmortizacion() {
    }

    /**
     * Calcula la cuota mensual fija con la fórmula estándar de amortización francesa:
     * <pre>
     *   cuota = monto * (i * (1 + i)^n) / ((1 + i)^n - 1)
     * </pre>
     * donde {@code i} es la tasa mensual (tasa anual / 12) y {@code n} es el plazo en meses.
     */
    public static BigDecimal calcularCuotaMensual(BigDecimal monto, BigDecimal tasaAnual, int plazoMeses) {
        BigDecimal tasaMensual = tasaMensual(tasaAnual);
        BigDecimal unoMasI = BigDecimal.ONE.add(tasaMensual);
        BigDecimal factor = unoMasI.pow(plazoMeses);
        BigDecimal numerador = monto.multiply(tasaMensual).multiply(factor);
        BigDecimal denominador = factor.subtract(BigDecimal.ONE);

        return numerador.divide(denominador, ReglasNegocioConstantes.ESCALA_CALCULO_INTERMEDIO,
                        ReglasNegocioConstantes.MODO_REDONDEO_MONETARIO)
                .setScale(ReglasNegocioConstantes.ESCALA_MONETARIA, ReglasNegocioConstantes.MODO_REDONDEO_MONETARIO);
    }

    /**
     * Genera el plan de pagos completo (una {@link PlanPagoCuota} por mes) a partir de la cuota
     * fija calculada en {@link #calcularCuotaMensual}. En cada cuota, el interés es el saldo
     * pendiente anterior multiplicado por la tasa mensual, y el abono a capital es la cuota
     * menos ese interés — salvo en la última cuota, donde el abono a capital se fuerza a ser
     * exactamente el saldo pendiente restante, para que el préstamo quede en cero sin arrastrar
     * diferencias de redondeo acumuladas a lo largo del plan.
     */
    public static List<PlanPagoCuota> generarPlanPagos(Long prestamoId, BigDecimal monto, BigDecimal tasaAnual,
                                                        int plazoMeses) {
        BigDecimal cuota = calcularCuotaMensual(monto, tasaAnual, plazoMeses);
        BigDecimal tasaMensual = tasaMensual(tasaAnual);
        AtomicReference<BigDecimal> saldoPendiente = new AtomicReference<>(
                monto.setScale(ReglasNegocioConstantes.ESCALA_MONETARIA, ReglasNegocioConstantes.MODO_REDONDEO_MONETARIO));

        return IntStream.rangeClosed(1, plazoMeses)
                .mapToObj(numeroCuota -> {
                    BigDecimal saldoAnterior = saldoPendiente.get();
                    boolean esUltimaCuota = numeroCuota == plazoMeses;

                    BigDecimal interes = saldoAnterior.multiply(tasaMensual)
                            .setScale(ReglasNegocioConstantes.ESCALA_MONETARIA, ReglasNegocioConstantes.MODO_REDONDEO_MONETARIO);
                    BigDecimal abonoCapital = esUltimaCuota
                            ? saldoAnterior
                            : cuota.subtract(interes)
                                    .setScale(ReglasNegocioConstantes.ESCALA_MONETARIA, ReglasNegocioConstantes.MODO_REDONDEO_MONETARIO);
                    BigDecimal nuevoSaldo = esUltimaCuota
                            ? BigDecimal.ZERO.setScale(ReglasNegocioConstantes.ESCALA_MONETARIA, ReglasNegocioConstantes.MODO_REDONDEO_MONETARIO)
                            : saldoAnterior.subtract(abonoCapital)
                                    .setScale(ReglasNegocioConstantes.ESCALA_MONETARIA, ReglasNegocioConstantes.MODO_REDONDEO_MONETARIO);

                    saldoPendiente.set(nuevoSaldo);

                    return new PlanPagoCuota(null, prestamoId, numeroCuota, cuota, interes, abonoCapital, nuevoSaldo);
                })
                .toList();
    }

    private static BigDecimal tasaMensual(BigDecimal tasaAnual) {
        return tasaAnual.divide(BigDecimal.valueOf(ReglasNegocioConstantes.MESES_POR_ANIO),
                ReglasNegocioConstantes.ESCALA_CALCULO_INTERMEDIO, ReglasNegocioConstantes.MODO_REDONDEO_MONETARIO);
    }
}
