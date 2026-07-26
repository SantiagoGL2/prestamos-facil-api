package com.prestamosfacil.infrastructure.oracle.mapper;

import com.prestamosfacil.infrastructure.oracle.entity.TipoPrestamoEntity;
import com.prestamosfacil.model.TipoPrestamo;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TipoPrestamoEntityMapper {

    TipoPrestamo toDomain(TipoPrestamoEntity entity);

    TipoPrestamoEntity toEntity(TipoPrestamo domain);
}
