package com.prestamosfacil.ports;

import com.prestamosfacil.model.NotificacionRegistroEvento;
import com.prestamosfacil.model.NotificacionSolicitudEvento;

public interface INotificacionPublisherPort {

    void publicarSolicitudResuelta(NotificacionSolicitudEvento evento);

    void publicarUsuarioRegistrado(NotificacionRegistroEvento evento);
}
