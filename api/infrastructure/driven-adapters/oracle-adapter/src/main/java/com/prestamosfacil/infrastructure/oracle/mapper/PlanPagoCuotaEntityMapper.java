package com.prestamosfacil.infrastructure.oracle.mapper;

import com.prestamosfacil.infrastructure.oracle.entity.PlanPagoCuotaEntity;
import com.prestamosfacil.model.PlanPagoCuota;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlanPagoCuotaEntityMapper {

    @Mapping(source = "prestamo.id", target = "prestamoId")
    PlanPagoCuota toDomain(PlanPagoCuotaEntity entity);

    @Mapping(target = "prestamo", ignore = true)
    PlanPagoCuotaEntity toEntity(PlanPagoCuota domain);
}
