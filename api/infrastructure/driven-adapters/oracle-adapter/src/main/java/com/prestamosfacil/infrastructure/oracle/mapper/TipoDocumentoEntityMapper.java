package com.prestamosfacil.infrastructure.oracle.mapper;

import com.prestamosfacil.infrastructure.oracle.entity.TipoDocumentoEntity;
import com.prestamosfacil.model.TipoDocumento;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TipoDocumentoEntityMapper {

    TipoDocumento toDomain(TipoDocumentoEntity entity);

    TipoDocumentoEntity toEntity(TipoDocumento domain);
}
