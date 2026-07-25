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
import jakarta.persistence.UniqueConstraint;

import com.prestamosfacil.enums.RolUsuario;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "usuario", uniqueConstraints = {
        @UniqueConstraint(name = "uq_usuario_doc", columnNames = {"tipo_documento_id", "numero_documento"})
})
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @Column(name = "nombres", length = 100, nullable = false)
    @ToString.Include
    private String nombres;

    @Column(name = "apellidos", length = 100, nullable = false)
    @ToString.Include
    private String apellidos;

    @Column(name = "email", length = 150, nullable = false, unique = true)
    @ToString.Include
    private String email;

    @ManyToOne
    @JoinColumn(name = "tipo_documento_id", nullable = false)
    private TipoDocumentoEntity tipoDocumento;

    @Column(name = "numero_documento", length = 30, nullable = false)
    @ToString.Include
    private String numeroDocumento;

    @Column(name = "salario_base", precision = 14, scale = 2, nullable = false)
    @ToString.Include
    private BigDecimal salarioBase;

    @Column(name = "password_hash", length = 255, nullable = false)
    @ToString.Include
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", length = 20, nullable = false)
    @ToString.Include
    private RolUsuario rol;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    @ToString.Include
    private LocalDateTime fechaCreacion;

    public UsuarioEntity(String nombres, String apellidos, String email, TipoDocumentoEntity tipoDocumento,
                          String numeroDocumento, BigDecimal salarioBase, String passwordHash, RolUsuario rol,
                          LocalDateTime fechaCreacion) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.salarioBase = salarioBase;
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.fechaCreacion = fechaCreacion;
    }
}