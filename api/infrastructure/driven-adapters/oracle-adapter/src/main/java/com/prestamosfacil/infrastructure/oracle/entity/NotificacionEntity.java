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

import com.prestamosfacil.enums.EstadoNotificacion;

import java.time.LocalDateTime;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "notificacion")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class NotificacionEntity {

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
    @JoinColumn(name = "solicitud_id")
    private SolicitudPrestamoEntity solicitud;

    @Column(name = "tipo", length = 30, nullable = false)
    @ToString.Include
    private String tipo;

    @Column(name = "mensaje", length = 500)
    @ToString.Include
    private String mensaje;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_envio", length = 20, nullable = false)
    @ToString.Include
    private EstadoNotificacion estadoEnvio;

    @Column(name = "fecha_envio")
    @ToString.Include
    private LocalDateTime fechaEnvio;

    public NotificacionEntity(UsuarioEntity usuario, SolicitudPrestamoEntity solicitud, String tipo, String mensaje,
                               EstadoNotificacion estadoEnvio, LocalDateTime fechaEnvio) {
        this.usuario = usuario;
        this.solicitud = solicitud;
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.estadoEnvio = estadoEnvio;
        this.fechaEnvio = fechaEnvio;
    }
}