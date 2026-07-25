package com.prestamosfacil.infrastructure.oracle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "plan_pago_cuota", uniqueConstraints = {
        @UniqueConstraint(name = "uq_plan_cuota", columnNames = {"prestamo_id", "numero_cuota"})
})
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class PlanPagoCuotaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prestamo_id", nullable = false)
    private PrestamoEntity prestamo;

    @Column(name = "numero_cuota", precision = 4, nullable = false)
    @ToString.Include
    private Integer numeroCuota;

    @Column(name = "cuota", precision = 14, scale = 2, nullable = false)
    @ToString.Include
    private BigDecimal cuota;

    @Column(name = "interes", precision = 14, scale = 2, nullable = false)
    @ToString.Include
    private BigDecimal interes;

    @Column(name = "abono_capital", precision = 14, scale = 2, nullable = false)
    @ToString.Include
    private BigDecimal abonoCapital;

    @Column(name = "saldo_pendiente", precision = 14, scale = 2, nullable = false)
    @ToString.Include
    private BigDecimal saldoPendiente;

    public PlanPagoCuotaEntity(PrestamoEntity prestamo, Integer numeroCuota, BigDecimal cuota, BigDecimal interes,
                                BigDecimal abonoCapital, BigDecimal saldoPendiente) {
        this.prestamo = prestamo;
        this.numeroCuota = numeroCuota;
        this.cuota = cuota;
        this.interes = interes;
        this.abonoCapital = abonoCapital;
        this.saldoPendiente = saldoPendiente;
    }
}