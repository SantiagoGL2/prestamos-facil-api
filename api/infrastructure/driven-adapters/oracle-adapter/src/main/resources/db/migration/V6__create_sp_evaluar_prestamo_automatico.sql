CREATE OR REPLACE PROCEDURE sp_evaluar_prestamo_automatico (
    p_solicitud_id          IN  NUMBER,
    p_estado_resultante     OUT VARCHAR2,
    p_capacidad_maxima      OUT NUMBER,
    p_deuda_actual          OUT NUMBER,
    p_capacidad_disponible  OUT NUMBER,
    p_cuota_nueva           OUT NUMBER
) AS
    v_usuario_id    NUMBER;
    v_salario_base  NUMBER;
    v_monto         NUMBER;
    v_plazo_meses   NUMBER;
    v_tasa_anual    NUMBER;
    v_tasa_mensual  NUMBER;
BEGIN
    SELECT sp.usuario_id, u.salario_base, sp.monto, sp.plazo_meses, tp.tasa_interes_anual
    INTO v_usuario_id, v_salario_base, v_monto, v_plazo_meses, v_tasa_anual
    FROM solicitud_prestamo sp
    JOIN usuario u ON u.id = sp.usuario_id
    JOIN tipo_prestamo tp ON tp.id = sp.tipo_prestamo_id
    WHERE sp.id = p_solicitud_id;

    v_tasa_mensual := v_tasa_anual / 12;

    p_capacidad_maxima := v_salario_base * 0.35;

    SELECT NVL(SUM(p.cuota_mensual), 0)
    INTO p_deuda_actual
    FROM prestamo p
    JOIN solicitud_prestamo sol ON sol.id = p.solicitud_id
    WHERE sol.usuario_id = v_usuario_id
      AND p.estado = 'APROBADO';

    p_capacidad_disponible := p_capacidad_maxima - p_deuda_actual;

    p_cuota_nueva := v_monto * (v_tasa_mensual * POWER(1 + v_tasa_mensual, v_plazo_meses))
                     / (POWER(1 + v_tasa_mensual, v_plazo_meses) - 1);

    IF p_cuota_nueva <= p_capacidad_disponible THEN
        IF v_monto > (v_salario_base * 5) THEN
            p_estado_resultante := 'REVISION_MANUAL';
        ELSE
            p_estado_resultante := 'APROBADO';
        END IF;
    ELSE
        p_estado_resultante := 'RECHAZADO';
    END IF;
END sp_evaluar_prestamo_automatico;
