package com.prestamosfacil.application.adapter;

import com.prestamosfacil.application.port.ITipoDocumentoPort;
import com.prestamosfacil.model.TipoDocumento;
import com.prestamosfacil.ports.ITipoDocumentoPersistencePort;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

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
