package com.prestamosfacil.infrastructure.oracle.repository;

import com.prestamosfacil.infrastructure.oracle.entity.NotificacionEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface INotificacionRepository extends JpaRepository<NotificacionEntity, Long> {
}
