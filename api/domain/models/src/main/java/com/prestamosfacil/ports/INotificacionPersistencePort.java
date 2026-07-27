package com.prestamosfacil.ports;

import com.prestamosfacil.model.Notificacion;

public interface INotificacionPersistencePort {

    Notificacion guardar(Notificacion notificacion);
}
