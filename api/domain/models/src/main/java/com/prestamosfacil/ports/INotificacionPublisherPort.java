package com.prestamosfacil.ports;

import com.prestamosfacil.model.Notificacion;

public interface INotificacionPublisherPort {

    void publicarSolicitudResuelta(Notificacion notificacion);
}
