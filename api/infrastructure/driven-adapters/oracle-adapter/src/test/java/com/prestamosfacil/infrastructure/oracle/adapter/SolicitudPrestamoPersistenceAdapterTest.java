package com.prestamosfacil.infrastructure.oracle.adapter;

import com.prestamosfacil.enums.EstadoSolicitud;
import com.prestamosfacil.infrastructure.oracle.entity.SolicitudPrestamoEntity;
import com.prestamosfacil.infrastructure.oracle.entity.TipoPrestamoEntity;
import com.prestamosfacil.infrastructure.oracle.entity.UsuarioEntity;
import com.prestamosfacil.infrastructure.oracle.mapper.SolicitudPrestamoEntityMapper;
import com.prestamosfacil.infrastructure.oracle.mocks.PrestamoFacilMocks;
import com.prestamosfacil.infrastructure.oracle.repository.ISolicitudPrestamoRepository;
import com.prestamosfacil.infrastructure.oracle.repository.ITipoPrestamoRepository;
import com.prestamosfacil.infrastructure.oracle.repository.IUsuarioRepository;
import com.prestamosfacil.model.SolicitudPrestamo;
import com.prestamosfacil.model.pagination.Paginacion;
import com.prestamosfacil.model.pagination.ResultadoPaginado;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolicitudPrestamoPersistenceAdapterTest {

    @Mock
    private ISolicitudPrestamoRepository solicitudPrestamoRepository;

    @Mock
    private IUsuarioRepository usuarioRepository;

    @Mock
    private ITipoPrestamoRepository tipoPrestamoRepository;

    @Mock
    private SolicitudPrestamoEntityMapper solicitudPrestamoEntityMapper;

    @InjectMocks
    private SolicitudPrestamoPersistenceAdapter solicitudPrestamoPersistenceAdapter;

    @Test
    void guardarSinAnalistaSoloResuelveLaReferenciaDelUsuario() {
        SolicitudPrestamo solicitud = PrestamoFacilMocks.getMockSolicitudPrestamo();
        SolicitudPrestamoEntity entitySinGuardar = PrestamoFacilMocks.getMockSolicitudPrestamoEntity();
        UsuarioEntity usuarioEntity = PrestamoFacilMocks.getMockUsuarioEntity();
        TipoPrestamoEntity tipoPrestamoEntity = PrestamoFacilMocks.getMockTipoPrestamoEntity();
        SolicitudPrestamoEntity entityGuardada = PrestamoFacilMocks.getMockSolicitudPrestamoEntity();

        when(solicitudPrestamoEntityMapper.toEntity(solicitud)).thenReturn(entitySinGuardar);
        when(usuarioRepository.getReferenceById(1L)).thenReturn(usuarioEntity);
        when(tipoPrestamoRepository.getReferenceById(1L)).thenReturn(tipoPrestamoEntity);
        when(solicitudPrestamoRepository.save(entitySinGuardar)).thenReturn(entityGuardada);
        when(solicitudPrestamoEntityMapper.toDomain(entityGuardada)).thenReturn(solicitud);

        SolicitudPrestamo resultado = solicitudPrestamoPersistenceAdapter.guardar(solicitud);

        assertEquals(solicitud, resultado);
        verify(usuarioRepository, times(1)).getReferenceById(any());
    }

    @Test
    void guardarConAnalistaResuelveTambienEsaReferencia() {
        SolicitudPrestamo solicitud = PrestamoFacilMocks.getMockSolicitudPrestamo().resuelta(EstadoSolicitud.APROBADO,
                5L);
        SolicitudPrestamoEntity entitySinGuardar = PrestamoFacilMocks.getMockSolicitudPrestamoEntity();
        UsuarioEntity usuarioEntity = PrestamoFacilMocks.getMockUsuarioEntity();
        TipoPrestamoEntity tipoPrestamoEntity = PrestamoFacilMocks.getMockTipoPrestamoEntity();

        when(solicitudPrestamoEntityMapper.toEntity(solicitud)).thenReturn(entitySinGuardar);
        when(usuarioRepository.getReferenceById(any())).thenReturn(usuarioEntity);
        when(tipoPrestamoRepository.getReferenceById(1L)).thenReturn(tipoPrestamoEntity);
        when(solicitudPrestamoRepository.save(entitySinGuardar)).thenReturn(entitySinGuardar);
        when(solicitudPrestamoEntityMapper.toDomain(entitySinGuardar)).thenReturn(solicitud);

        solicitudPrestamoPersistenceAdapter.guardar(solicitud);

        verify(usuarioRepository, times(2)).getReferenceById(any());
        verify(usuarioRepository).getReferenceById(5L);
    }

    @Test
    void buscarPorIdRetornaVacioCuandoNoExiste() {
        when(solicitudPrestamoRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<SolicitudPrestamo> resultado = solicitudPrestamoPersistenceAdapter.buscarPorId(999L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void listarPorEstadoConEstadoNuloUsaFindAll() {
        SolicitudPrestamoEntity entity = PrestamoFacilMocks.getMockSolicitudPrestamoEntity();
        SolicitudPrestamo dominio = PrestamoFacilMocks.getMockSolicitudPrestamo();
        Page<SolicitudPrestamoEntity> pagina = new PageImpl<>(List.of(entity));
        when(solicitudPrestamoRepository.findAll(any(Pageable.class))).thenReturn(pagina);
        when(solicitudPrestamoEntityMapper.toDomain(entity)).thenReturn(dominio);

        ResultadoPaginado<SolicitudPrestamo> resultado = solicitudPrestamoPersistenceAdapter.listarPorEstado(null,
                new Paginacion(0, 20));

        assertEquals(1, resultado.contenido().size());
        verify(solicitudPrestamoRepository, never()).findByEstado(any(), any());
    }

    @Test
    void listarPorEstadoConEstadoDadoUsaFindByEstado() {
        SolicitudPrestamoEntity entity = PrestamoFacilMocks.getMockSolicitudPrestamoEntity();
        SolicitudPrestamo dominio = PrestamoFacilMocks.getMockSolicitudPrestamo();
        Page<SolicitudPrestamoEntity> pagina = new PageImpl<>(List.of(entity));
        when(solicitudPrestamoRepository.findByEstado(EstadoSolicitud.PENDIENTE_REVISION,
                PageRequest.of(0, 20))).thenReturn(pagina);
        when(solicitudPrestamoEntityMapper.toDomain(entity)).thenReturn(dominio);

        ResultadoPaginado<SolicitudPrestamo> resultado = solicitudPrestamoPersistenceAdapter.listarPorEstado(
                EstadoSolicitud.PENDIENTE_REVISION, new Paginacion(0, 20));

        assertEquals(1, resultado.contenido().size());
    }

    @Test
    void listarPorFechaDelegaEnElRepositorioConElRangoDelDia() {
        SolicitudPrestamoEntity entity = PrestamoFacilMocks.getMockSolicitudPrestamoEntity();
        SolicitudPrestamo dominio = PrestamoFacilMocks.getMockSolicitudPrestamo();
        LocalDate fechaDesde = LocalDate.now().minusDays(10);
        LocalDate fechaHasta = LocalDate.now();
        Page<SolicitudPrestamoEntity> pagina = new PageImpl<>(List.of(entity));
        when(solicitudPrestamoRepository.findByFechaSolicitudBetween(fechaDesde.atStartOfDay(),
                fechaHasta.atTime(23, 59, 59), PageRequest.of(0, 20))).thenReturn(pagina);
        when(solicitudPrestamoEntityMapper.toDomain(entity)).thenReturn(dominio);

        ResultadoPaginado<SolicitudPrestamo> resultado = solicitudPrestamoPersistenceAdapter.listarPorFecha(
                fechaDesde, fechaHasta, new Paginacion(0, 20));

        assertEquals(1, resultado.contenido().size());
    }
}
