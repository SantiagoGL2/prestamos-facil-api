package com.prestamosfacil.infrastructure.oracle.adapter;

import com.prestamosfacil.enums.EstadoSolicitud;
import com.prestamosfacil.infrastructure.oracle.entity.SolicitudPrestamoEntity;
import com.prestamosfacil.infrastructure.oracle.mapper.SolicitudPrestamoEntityMapper;
import com.prestamosfacil.infrastructure.oracle.repository.ISolicitudPrestamoRepository;
import com.prestamosfacil.infrastructure.oracle.repository.ITipoPrestamoRepository;
import com.prestamosfacil.infrastructure.oracle.repository.IUsuarioRepository;
import com.prestamosfacil.model.SolicitudPrestamo;
import com.prestamosfacil.model.pagination.Paginacion;
import com.prestamosfacil.model.pagination.ResultadoPaginado;
import com.prestamosfacil.ports.ISolicitudPrestamoPersistencePort;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class SolicitudPrestamoPersistenceAdapter implements ISolicitudPrestamoPersistencePort {

    private final ISolicitudPrestamoRepository solicitudPrestamoRepository;
    private final IUsuarioRepository usuarioRepository;
    private final ITipoPrestamoRepository tipoPrestamoRepository;
    private final SolicitudPrestamoEntityMapper solicitudPrestamoEntityMapper;

    public SolicitudPrestamoPersistenceAdapter(ISolicitudPrestamoRepository solicitudPrestamoRepository,
                                                IUsuarioRepository usuarioRepository,
                                                ITipoPrestamoRepository tipoPrestamoRepository,
                                                SolicitudPrestamoEntityMapper solicitudPrestamoEntityMapper) {
        this.solicitudPrestamoRepository = solicitudPrestamoRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoPrestamoRepository = tipoPrestamoRepository;
        this.solicitudPrestamoEntityMapper = solicitudPrestamoEntityMapper;
    }

    @Override
    public SolicitudPrestamo guardar(SolicitudPrestamo solicitudPrestamo) {
        SolicitudPrestamoEntity entity = solicitudPrestamoEntityMapper.toEntity(solicitudPrestamo);
        entity.setUsuario(usuarioRepository.getReferenceById(solicitudPrestamo.usuario().id()));
        entity.setTipoPrestamo(tipoPrestamoRepository.getReferenceById(solicitudPrestamo.tipoPrestamo().id()));
        if (solicitudPrestamo.analistaId() != null) {
            entity.setAnalista(usuarioRepository.getReferenceById(solicitudPrestamo.analistaId()));
        }

        SolicitudPrestamoEntity guardada = solicitudPrestamoRepository.save(entity);

        return solicitudPrestamoEntityMapper.toDomain(guardada);
    }

    @Override
    public Optional<SolicitudPrestamo> buscarPorId(Long id) {
        return solicitudPrestamoRepository.findById(id).map(solicitudPrestamoEntityMapper::toDomain);
    }

    @Override
    public ResultadoPaginado<SolicitudPrestamo> listarPorEstado(EstadoSolicitud estadoOpcional,
                                                                 Paginacion paginacion) {
        PageRequest pageRequest = PageRequest.of(paginacion.pagina(), paginacion.tamano());

        Page<SolicitudPrestamoEntity> pagina = estadoOpcional == null
                ? solicitudPrestamoRepository.findAll(pageRequest)
                : solicitudPrestamoRepository.findByEstado(estadoOpcional, pageRequest);

        return mapearPagina(pagina);
    }

    @Override
    public ResultadoPaginado<SolicitudPrestamo> listarPorFecha(LocalDate fechaDesdeOpcional,
                                                                LocalDate fechaHastaOpcional,
                                                                Paginacion paginacion) {
        Page<SolicitudPrestamoEntity> pagina = solicitudPrestamoRepository.findByFechaSolicitudBetween(
                fechaDesdeOpcional.atStartOfDay(), fechaHastaOpcional.atTime(23, 59, 59),
                PageRequest.of(paginacion.pagina(), paginacion.tamano()));

        return mapearPagina(pagina);
    }

    private ResultadoPaginado<SolicitudPrestamo> mapearPagina(Page<SolicitudPrestamoEntity> pagina) {
        return new ResultadoPaginado<>(
                pagina.getContent().stream().map(solicitudPrestamoEntityMapper::toDomain).toList(),
                pagina.getTotalElements(), pagina.getTotalPages(), pagina.getNumber());
    }
}
