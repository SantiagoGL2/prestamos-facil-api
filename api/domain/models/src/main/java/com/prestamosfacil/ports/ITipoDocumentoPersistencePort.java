package com.prestamosfacil.ports;

import com.prestamosfacil.model.TipoDocumento;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de persistencia para el catálogo de tipos de documento de identidad. Quien lo
 * implemente solo necesita garantizar lectura consistente del catálogo — no hay altas ni
 * modificaciones expuestas porque este catálogo se administra fuera de la API.
 */
public interface ITipoDocumentoPersistencePort {

    Optional<TipoDocumento> buscarPorId(Long id);

    List<TipoDocumento> listarActivos();
}
