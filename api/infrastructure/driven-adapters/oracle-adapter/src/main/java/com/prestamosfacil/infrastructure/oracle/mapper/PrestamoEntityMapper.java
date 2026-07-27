package com.prestamosfacil.infrastructure.oracle.mapper;

import com.prestamosfacil.infrastructure.oracle.entity.PrestamoEntity;
import com.prestamosfacil.model.Prestamo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PrestamoEntityMapper {

    @Mapping(source = "solicitud.id", target = "solicitudId")
    Prestamo toDomain(PrestamoEntity entity);

    @Mapping(target = "solicitud", ignore = true)
    PrestamoEntity toEntity(Prestamo domain);
}
