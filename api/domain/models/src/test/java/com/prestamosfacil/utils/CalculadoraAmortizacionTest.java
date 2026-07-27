package com.prestamosfacil.utils;

import com.prestamosfacil.model.PlanPagoCuota;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CalculadoraAmortizacionTest {

    @Test
    void calcularCuotaMensualConMillonAlVeinticuatroPorCientoAnualDoceMeses() {
        BigDecimal cuota = CalculadoraAmortizacion.calcularCuotaMensual(BigDecimal.valueOf(1_000_000),
                BigDecimal.valueOf(0.24), 12);

        assertEquals(0, cuota.compareTo(new BigDecimal("94559.60")));
    }

    @Test
    void generarPlanPagosCalculaElInteresDeLaPrimeraCuota() {
        List<PlanPagoCuota> plan = CalculadoraAmortizacion.generarPlanPagos(1L, BigDecimal.valueOf(1_000_000),
                BigDecimal.valueOf(0.24), 12);

        assertEquals(0, plan.getFirst().interes().compareTo(new BigDecimal("20000.00")));
    }

    @Test
    void generarPlanPagosLaSumaDeAbonosACapitalEsIgualAlMontoOriginal() {
        List<PlanPagoCuota> plan = CalculadoraAmortizacion.generarPlanPagos(1L, BigDecimal.valueOf(1_000_000),
                BigDecimal.valueOf(0.24), 12);

        BigDecimal sumaAbonos = plan.stream()
                .map(PlanPagoCuota::abonoCapital)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal diferencia = sumaAbonos.subtract(BigDecimal.valueOf(1_000_000)).abs();
        assertTrue(diferencia.compareTo(new BigDecimal("0.05")) <= 0);
    }
}
