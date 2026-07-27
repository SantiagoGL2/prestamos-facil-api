package com.prestamosfacil.infrastructure.oracle.adapter;

import com.prestamosfacil.enums.RolUsuario;
import com.prestamosfacil.infrastructure.oracle.entity.TipoDocumentoEntity;
import com.prestamosfacil.infrastructure.oracle.entity.UsuarioEntity;
import com.prestamosfacil.infrastructure.oracle.mapper.UsuarioEntityMapper;
import com.prestamosfacil.infrastructure.oracle.mocks.PrestamoFacilMocks;
import com.prestamosfacil.infrastructure.oracle.repository.ITipoDocumentoRepository;
import com.prestamosfacil.infrastructure.oracle.repository.IUsuarioRepository;
import com.prestamosfacil.model.Usuario;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioPersistenceAdapterTest {

    @Mock
    private IUsuarioRepository usuarioRepository;

    @Mock
    private ITipoDocumentoRepository tipoDocumentoRepository;

    @Mock
    private UsuarioEntityMapper usuarioEntityMapper;

    @InjectMocks
    private UsuarioPersistenceAdapter usuarioPersistenceAdapter;

    @Test
    void guardarResuelveLaReferenciaDelTipoDocumentoYRetornaElUsuarioMapeado() {
        Usuario usuario = PrestamoFacilMocks.getMockUsuarioCliente();
        UsuarioEntity entitySinGuardar = PrestamoFacilMocks.getMockUsuarioEntity();
        TipoDocumentoEntity tipoDocumentoEntity = PrestamoFacilMocks.getMockTipoDocumentoEntity();
        UsuarioEntity entityGuardada = PrestamoFacilMocks.getMockUsuarioEntity();

        when(usuarioEntityMapper.toEntity(usuario)).thenReturn(entitySinGuardar);
        when(tipoDocumentoRepository.getReferenceById(1L)).thenReturn(tipoDocumentoEntity);
        when(usuarioRepository.save(entitySinGuardar)).thenReturn(entityGuardada);
        when(usuarioEntityMapper.toDomain(entityGuardada)).thenReturn(usuario);

        Usuario resultado = usuarioPersistenceAdapter.guardar(usuario);

        assertEquals(usuario, resultado);
        assertEquals(tipoDocumentoEntity, entitySinGuardar.getTipoDocumento());
    }

    @Test
    void buscarPorIdRetornaElUsuarioCuandoExiste() {
        UsuarioEntity entity = PrestamoFacilMocks.getMockUsuarioEntity();
        Usuario dominio = PrestamoFacilMocks.getMockUsuarioCliente();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(usuarioEntityMapper.toDomain(entity)).thenReturn(dominio);

        Optional<Usuario> resultado = usuarioPersistenceAdapter.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(dominio, resultado.get());
    }

    @Test
    void buscarPorEmailRetornaVacioCuandoNoExiste() {
        when(usuarioRepository.findByEmail("desconocido@mail.com")).thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioPersistenceAdapter.buscarPorEmail("desconocido@mail.com");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void buscarPorTipoDocumentoYNumeroDocumentoRetornaElUsuarioCuandoExiste() {
        UsuarioEntity entity = PrestamoFacilMocks.getMockUsuarioEntity();
        Usuario dominio = PrestamoFacilMocks.getMockUsuarioCliente();
        when(usuarioRepository.findByTipoDocumentoIdAndNumeroDocumento(1L, "123")).thenReturn(Optional.of(entity));
        when(usuarioEntityMapper.toDomain(entity)).thenReturn(dominio);

        Optional<Usuario> resultado = usuarioPersistenceAdapter.buscarPorTipoDocumentoYNumeroDocumento(1L, "123");

        assertTrue(resultado.isPresent());
        assertEquals(dominio, resultado.get());
    }

    @Test
    void existeEmailDelegaEnElRepositorio() {
        when(usuarioRepository.existsByEmail("juan@mail.com")).thenReturn(true);

        assertTrue(usuarioPersistenceAdapter.existeEmail("juan@mail.com"));
    }

    @Test
    void existeTipoDocumentoYNumeroDocumentoDelegaEnElRepositorio() {
        when(usuarioRepository.existsByTipoDocumentoIdAndNumeroDocumento(1L, "123")).thenReturn(false);

        assertFalse(usuarioPersistenceAdapter.existeTipoDocumentoYNumeroDocumento(1L, "123"));
    }

    @Test
    void listarPorRolMapeaLosResultadosDelRepositorio() {
        UsuarioEntity entity = PrestamoFacilMocks.getMockUsuarioEntity();
        Usuario dominio = PrestamoFacilMocks.getMockUsuarioCliente();
        when(usuarioRepository.findByRol(RolUsuario.CLIENTE)).thenReturn(List.of(entity));
        when(usuarioEntityMapper.toDomain(entity)).thenReturn(dominio);

        List<Usuario> resultado = usuarioPersistenceAdapter.listarPorRol(RolUsuario.CLIENTE);

        assertEquals(List.of(dominio), resultado);
        verify(usuarioRepository).findByRol(RolUsuario.CLIENTE);
    }
}
