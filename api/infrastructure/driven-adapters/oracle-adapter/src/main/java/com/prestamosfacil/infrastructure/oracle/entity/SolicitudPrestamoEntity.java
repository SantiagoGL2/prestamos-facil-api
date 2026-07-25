package com.prestamosfacil.infrastructure.oracle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.prestamosfacil.enums.EstadoSolicitud;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "solicitud_prestamo")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SolicitudPrestamoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    @ManyToOne
    @JoinColumn(name = "tipo_prestamo_id", nullable = false)
    private TipoPrestamoEntity tipoPrestamo;

    @Column(name = "monto", precision = 14, scale = 2, nullable = false)
    @ToString.Include
    private BigDecimal monto;

    @Column(name = "plazo_meses", precision = 4, nullable = false)
    @ToString.Include
    private Integer plazoMeses;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 25, nullable = false)
    @ToString.Include
    private EstadoSolicitud estado;

    @ManyToOne
    @JoinColumn(name = "analista_id")
    private UsuarioEntity analista;

    @Column(name = "fecha_solicitud", nullable = false, updatable = false)
    @ToString.Include
    private LocalDateTime fechaSolicitud;

    @Column(name = "fecha_resolucion")
    @ToString.Include
    private LocalDateTime fechaResolucion;

    public SolicitudPrestamoEntity(UsuarioEntity usuario, TipoPrestamoEntity tipoPrestamo, BigDecimal monto,
                                    Integer plazoMeses, EstadoSolicitud estado, UsuarioEntity analista,
                                    LocalDateTime fechaSolicitud, LocalDateTime fechaResolucion) {
        this.usuario = usuario;
        this.tipoPrestamo = tipoPrestamo;
        this.monto = monto;
        this.plazoMeses = plazoMeses;
        this.estado = estado;
        this.analista = analista;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaResolucion = fechaResolucion;
    }
}