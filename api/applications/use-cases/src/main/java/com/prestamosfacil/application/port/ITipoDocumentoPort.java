package com.prestamosfacil.application.port;

import com.prestamosfacil.model.TipoDocumento;

import java.util.List;

public interface ITipoDocumentoPort {

    List<TipoDocumento> listarActivos();
}
