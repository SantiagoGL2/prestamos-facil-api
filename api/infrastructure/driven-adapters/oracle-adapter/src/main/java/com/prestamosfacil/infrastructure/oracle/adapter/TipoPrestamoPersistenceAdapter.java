package com.prestamosfacil.infrastructure.oracle.adapter;

import com.prestamosfacil.infrastructure.oracle.mapper.TipoPrestamoEntityMapper;
import com.prestamosfacil.infrastructure.oracle.repository.ITipoPrestamoRepository;
import com.prestamosfacil.model.TipoPrestamo;
import com.prestamosfacil.ports.ITipoPrestamoPersistencePort;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TipoPrestamoPersistenceAdapter implements ITipoPrestamoPersistencePort {

    private final ITipoPrestamoRepository tipoPrestamoRepository;
    private final TipoPrestamoEntityMapper tipoPrestamoEntityMapper;

    public TipoPrestamoPersistenceAdapter(ITipoPrestamoRepository tipoPrestamoRepository,
                                           TipoPrestamoEntityMapper tipoPrestamoEntityMapper) {
        this.tipoPrestamoRepository = tipoPrestamoRepository;
        this.tipoPrestamoEntityMapper = tipoPrestamoEntityMapper;
    }

    @Override
    public Optional<TipoPrestamo> buscarPorId(Long id) {
        return tipoPrestamoRepository.findById(id).map(tipoPrestamoEntityMapper::toDomain);
    }

    @Override
    public List<TipoPrestamo> listarActivos() {
        return tipoPrestamoRepository.findByActivoTrue().stream()
                .map(tipoPrestamoEntityMapper::toDomain)
                .toList();
    }
}
