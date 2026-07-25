package com.prestamosfacil.infrastructure.oracle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import com.prestamosfacil.enums.EstadoPrestamo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "prestamo")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class PrestamoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @OneToOne
    @JoinColumn(name = "solicitud_id", nullable = false, unique = true)
    private SolicitudPrestamoEntity solicitud;

    @Column(name = "monto_aprobado", precision = 14, scale = 2, nullable = false)
    @ToString.Include
    private BigDecimal montoAprobado;

    @Column(name = "tasa_interes_mensual", precision = 8, scale = 6, nullable = false)
    @ToString.Include
    private BigDecimal tasaInteresMensual;

    @Column(name = "cuota_mensual", precision = 14, scale = 2, nullable = false)
    @ToString.Include
    private BigDecimal cuotaMensual;

    @Column(name = "plazo_meses", precision = 4, nullable = false)
    @ToString.Include
    private Integer plazoMeses;

    @Column(name = "fecha_aprobacion", nullable = false, updatable = false)
    @ToString.Include
    private LocalDateTime fechaAprobacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 20, nullable = false)
    @ToString.Include
    private EstadoPrestamo estado;

    public PrestamoEntity(SolicitudPrestamoEntity solicitud, BigDecimal montoAprobado, BigDecimal tasaInteresMensual,
                           BigDecimal cuotaMensual, Integer plazoMeses, LocalDateTime fechaAprobacion,
                           EstadoPrestamo estado) {
        this.solicitud = solicitud;
        this.montoAprobado = montoAprobado;
        this.tasaInteresMensual = tasaInteresMensual;
        this.cuotaMensual = cuotaMensual;
        this.plazoMeses = plazoMeses;
        this.fechaAprobacion = fechaAprobacion;
        this.estado = estado;
    }
}