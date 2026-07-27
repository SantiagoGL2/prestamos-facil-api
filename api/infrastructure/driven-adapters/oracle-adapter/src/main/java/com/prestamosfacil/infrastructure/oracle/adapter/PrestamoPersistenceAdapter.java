package com.prestamosfacil.infrastructure.oracle.adapter;

import com.prestamosfacil.enums.EstadoPrestamo;
import com.prestamosfacil.infrastructure.oracle.entity.PrestamoEntity;
import com.prestamosfacil.infrastructure.oracle.mapper.PrestamoEntityMapper;
import com.prestamosfacil.infrastructure.oracle.repository.IPrestamoRepository;
import com.prestamosfacil.infrastructure.oracle.repository.ISolicitudPrestamoRepository;
import com.prestamosfacil.model.Prestamo;
import com.prestamosfacil.ports.IPrestamoPersistencePort;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PrestamoPersistenceAdapter implements IPrestamoPersistencePort {

    private final IPrestamoRepository prestamoRepository;
    private final ISolicitudPrestamoRepository solicitudPrestamoRepository;
    private final PrestamoEntityMapper prestamoEntityMapper;

    public PrestamoPersistenceAdapter(IPrestamoRepository prestamoRepository,
                                       ISolicitudPrestamoRepository solicitudPrestamoRepository,
                                       PrestamoEntityMapper prestamoEntityMapper) {
        this.prestamoRepository = prestamoRepository;
        this.solicitudPrestamoRepository = solicitudPrestamoRepository;
        this.prestamoEntityMapper = prestamoEntityMapper;
    }

    @Override
    public Prestamo guardar(Prestamo prestamo) {
        PrestamoEntity entity = prestamoEntityMapper.toEntity(prestamo);
        entity.setSolicitud(solicitudPrestamoRepository.getReferenceById(prestamo.solicitudId()));

        PrestamoEntity guardado = prestamoRepository.save(entity);

        return prestamoEntityMapper.toDomain(guardado);
    }

    @Override
    public Optional<Prestamo> buscarPorSolicitudId(Long solicitudId) {
        return prestamoRepository.findBySolicitudId(solicitudId).map(prestamoEntityMapper::toDomain);
    }

    @Override
    public List<Prestamo> listarActivos() {
        return prestamoRepository.findByEstado(EstadoPrestamo.APROBADO).stream()
                .map(prestamoEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<Prestamo> listarAprobados() {
        return prestamoRepository.findAll().stream()
                .map(prestamoEntityMapper::toDomain)
                .toList();
    }
}
