package com.prestamosfacil.infrastructure.oracle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tipo_prestamo")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class TipoPrestamoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @Column(name = "nombre", length = 100, nullable = false)
    @ToString.Include
    private String nombre;

    @Column(name = "tasa_interes_anual", precision = 6, scale = 4, nullable = false)
    @ToString.Include
    private BigDecimal tasaInteresAnual;

    @Column(name = "validacion_automatica", nullable = false)
    @ToString.Include
    private boolean validacionAutomatica;

    @Column(name = "monto_min", precision = 14, scale = 2, nullable = false)
    @ToString.Include
    private BigDecimal montoMin;

    @Column(name = "monto_max", precision = 14, scale = 2, nullable = false)
    @ToString.Include
    private BigDecimal montoMax;

    @Column(name = "plazo_max_meses", precision = 4, nullable = false)
    @ToString.Include
    private Integer plazoMaxMeses;

    @Column(name = "activo", nullable = false)
    @ToString.Include
    private boolean activo;

    public TipoPrestamoEntity(String nombre, BigDecimal tasaInteresAnual, boolean validacionAutomatica,
                               BigDecimal montoMin, BigDecimal montoMax, Integer plazoMaxMeses, boolean activo) {
        this.nombre = nombre;
        this.tasaInteresAnual = tasaInteresAnual;
        this.validacionAutomatica = validacionAutomatica;
        this.montoMin = montoMin;
        this.montoMax = montoMax;
        this.plazoMaxMeses = plazoMaxMeses;
        this.activo = activo;
    }
}