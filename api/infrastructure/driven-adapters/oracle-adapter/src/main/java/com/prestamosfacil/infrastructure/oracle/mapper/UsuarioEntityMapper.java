package com.prestamosfacil.infrastructure.oracle.mapper;

import com.prestamosfacil.infrastructure.oracle.entity.UsuarioEntity;
import com.prestamosfacil.model.Usuario;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = TipoDocumentoEntityMapper.class)
public interface UsuarioEntityMapper {

    Usuario toDomain(UsuarioEntity entity);

    @Mapping(target = "tipoDocumento", ignore = true)
    UsuarioEntity toEntity(Usuario domain);
}
