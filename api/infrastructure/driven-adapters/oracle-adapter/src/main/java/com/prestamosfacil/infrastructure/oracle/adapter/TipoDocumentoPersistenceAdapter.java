package com.prestamosfacil.infrastructure.oracle.adapter;

import com.prestamosfacil.infrastructure.oracle.mapper.TipoDocumentoEntityMapper;
import com.prestamosfacil.infrastructure.oracle.repository.ITipoDocumentoRepository;
import com.prestamosfacil.model.TipoDocumento;
import com.prestamosfacil.ports.ITipoDocumentoPersistencePort;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TipoDocumentoPersistenceAdapter implements ITipoDocumentoPersistencePort {

    private final ITipoDocumentoRepository tipoDocumentoRepository;
    private final TipoDocumentoEntityMapper tipoDocumentoEntityMapper;

    public TipoDocumentoPersistenceAdapter(ITipoDocumentoRepository tipoDocumentoRepository,
                                            TipoDocumentoEntityMapper tipoDocumentoEntityMapper) {
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.tipoDocumentoEntityMapper = tipoDocumentoEntityMapper;
    }

    @Override
    public Optional<TipoDocumento> buscarPorId(Long id) {
        return tipoDocumentoRepository.findById(id).map(tipoDocumentoEntityMapper::toDomain);
    }

    @Override
    public List<TipoDocumento> listarActivos() {
        return tipoDocumentoRepository.findByActivoTrue().stream()
                .map(tipoDocumentoEntityMapper::toDomain)
                .toList();
    }
}
