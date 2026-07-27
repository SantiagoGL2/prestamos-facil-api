package com.prestamosfacil.infrastructure.oracle.mapper;

import com.prestamosfacil.infrastructure.oracle.entity.NotificacionEntity;
import com.prestamosfacil.model.Notificacion;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificacionEntityMapper {

    @Mapping(source = "usuario.id", target = "usuarioId")
    @Mapping(source = "solicitud.id", target = "solicitudId")
    Notificacion toDomain(NotificacionEntity entity);

    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "solicitud", ignore = true)
    NotificacionEntity toEntity(Notificacion domain);
}
