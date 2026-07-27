package com.prestamosfacil.application.adapter;

import com.prestamosfacil.application.port.ITipoDocumentoPort;
import com.prestamosfacil.model.TipoDocumento;
import com.prestamosfacil.ports.ITipoDocumentoPersistencePort;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Consulta el catálogo de tipos de documento. Es de solo lectura y prácticamente estático, por
 * eso {@link #listarActivos()} está cacheado — no hay altas/bajas expuestas por la API que
 * requieran invalidar ese cache.
 */
@Service
public class TipoDocumentoUseCase implements ITipoDocumentoPort {

    private final ITipoDocumentoPersistencePort tipoDocumentoPersistencePort;

    public TipoDocumentoUseCase(ITipoDocumentoPersistencePort tipoDocumentoPersistencePort) {
        this.tipoDocumentoPersistencePort = tipoDocumentoPersistencePort;
    }

    @Override
    @Cacheable(value = "tiposDocumento")
    public List<TipoDocumento> listarActivos() {
        return tipoDocumentoPersistencePort.listarActivos();
    }
}
