package com.prestamosfacil.constant;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class ReglasNegocioConstantes {

    public static final int MESES_POR_ANIO = 12;
    public static final int ESCALA_MONETARIA = 2;
    public static final RoundingMode MODO_REDONDEO_MONETARIO = RoundingMode.HALF_UP;
    public static final int ESCALA_CALCULO_INTERMEDIO = 10;
    public static final BigDecimal SALARIO_MINIMO = BigDecimal.ZERO;
    public static final BigDecimal SALARIO_MAXIMO = BigDecimal.valueOf(15_000_000);

    private ReglasNegocioConstantes() {
    }
}
