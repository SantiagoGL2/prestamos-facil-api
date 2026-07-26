package com.prestamosfacil.infrastructure.oracle.adapter;

import com.prestamosfacil.infrastructure.oracle.entity.UsuarioEntity;
import com.prestamosfacil.infrastructure.oracle.mapper.UsuarioEntityMapper;
import com.prestamosfacil.infrastructure.oracle.repository.ITipoDocumentoRepository;
import com.prestamosfacil.infrastructure.oracle.repository.IUsuarioRepository;
import com.prestamosfacil.model.Usuario;
import com.prestamosfacil.ports.IUsuarioPersistencePort;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsuarioPersistenceAdapter implements IUsuarioPersistencePort {

    private final IUsuarioRepository usuarioRepository;
    private final ITipoDocumentoRepository tipoDocumentoRepository;
    private final UsuarioEntityMapper usuarioEntityMapper;

    public UsuarioPersistenceAdapter(IUsuarioRepository usuarioRepository,
                                      ITipoDocumentoRepository tipoDocumentoRepository,
                                      UsuarioEntityMapper usuarioEntityMapper) {
        this.usuarioRepository = usuarioRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.usuarioEntityMapper = usuarioEntityMapper;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        UsuarioEntity entity = usuarioEntityMapper.toEntity(usuario);
        entity.setTipoDocumento(tipoDocumentoRepository.getReferenceById(usuario.tipoDocumento().id()));

        UsuarioEntity guardado = usuarioRepository.save(entity);

        return usuarioEntityMapper.toDomain(guardado);
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id).map(usuarioEntityMapper::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email).map(usuarioEntityMapper::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorTipoDocumentoYNumeroDocumento(Long tipoDocumentoId, String numeroDocumento) {
        return usuarioRepository.findByTipoDocumentoIdAndNumeroDocumento(tipoDocumentoId, numeroDocumento)
                .map(usuarioEntityMapper::toDomain);
    }

    @Override
    public boolean existeEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    @Override
    public boolean existeTipoDocumentoYNumeroDocumento(Long tipoDocumentoId, String numeroDocumento) {
        return usuarioRepository.existsByTipoDocumentoIdAndNumeroDocumento(tipoDocumentoId, numeroDocumento);
    }
}
