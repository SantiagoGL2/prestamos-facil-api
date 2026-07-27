package com.prestamosfacil.ports;

import com.prestamosfacil.model.Notificacion;

/**
 * Puerto de persistencia para el registro histórico de notificaciones. Toda notificación
 * disparada por la app (aprobación, rechazo, bienvenida) se guarda aquí con su resultado real
 * de envío (ENVIADA/FALLIDA), tanto si el correo salió bien como si falló — esto es una
 * bitácora de auditoría, no una cola de reintentos.
 */
public interface INotificacionPersistencePort {

    Notificacion guardar(Notificacion notificacion);
}
