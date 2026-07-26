package com.prestamosfacil.infrastructure.oracle.mapper;

import com.prestamosfacil.infrastructure.oracle.entity.SolicitudPrestamoEntity;
import com.prestamosfacil.model.SolicitudPrestamo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UsuarioEntityMapper.class, TipoPrestamoEntityMapper.class})
public interface SolicitudPrestamoEntityMapper {

    @Mapping(source = "analista.id", target = "analistaId")
    SolicitudPrestamo toDomain(SolicitudPrestamoEntity entity);

    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "tipoPrestamo", ignore = true)
    @Mapping(target = "analista", ignore = true)
    SolicitudPrestamoEntity toEntity(SolicitudPrestamo domain);
}
