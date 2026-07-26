package com.prestamosfacil.service;

import com.prestamosfacil.application.port.ITipoDocumentoPort;
import com.prestamosfacil.model.TipoDocumento;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TipoDocumentoService {

    private final ITipoDocumentoPort tipoDocumentoPort;

    public TipoDocumentoService(ITipoDocumentoPort tipoDocumentoPort) {
        this.tipoDocumentoPort = tipoDocumentoPort;
    }

    @Transactional(readOnly = true)
    public List<TipoDocumento> listarActivos() {
        return tipoDocumentoPort.listarActivos();
    }
}
