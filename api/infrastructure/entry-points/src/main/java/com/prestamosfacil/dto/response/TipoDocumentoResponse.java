package com.prestamosfacil.dto.response;

import com.prestamosfacil.model.TipoDocumento;

public record TipoDocumentoResponse(Long id, String codigo, String nombre) {

    public static TipoDocumentoResponse from(TipoDocumento tipoDocumento) {
        return new TipoDocumentoResponse(tipoDocumento.id(), tipoDocumento.codigo(), tipoDocumento.nombre());
    }
}
