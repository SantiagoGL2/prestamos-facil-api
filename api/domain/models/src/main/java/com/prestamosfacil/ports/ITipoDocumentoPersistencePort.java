package com.prestamosfacil.ports;

import com.prestamosfacil.model.TipoDocumento;

import java.util.List;
import java.util.Optional;

public interface ITipoDocumentoPersistencePort {

    Optional<TipoDocumento> buscarPorId(Long id);

    List<TipoDocumento> listarActivos();
}
