package com.prestamosfacil.ports;

import com.prestamosfacil.model.NotificacionRegistroEvento;
import com.prestamosfacil.model.NotificacionSolicitudEvento;

/**
 * Puerto de publicación de eventos de notificación. Quien lo implemente solo debe encolar el
 * evento (ej. a RabbitMQ) para que un consumidor externo lo procese de forma asíncrona — este
 * puerto no envía correos ni bloquea el hilo que registra la solicitud o el usuario.
 */
public interface INotificacionPublisherPort {

    void publicarSolicitudResuelta(NotificacionSolicitudEvento evento);

    void publicarUsuarioRegistrado(NotificacionRegistroEvento evento);
}
