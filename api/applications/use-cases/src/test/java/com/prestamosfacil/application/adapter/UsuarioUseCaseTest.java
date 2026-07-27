package com.prestamosfacil.application.adapter;

import com.prestamosfacil.enums.RolUsuario;
import com.prestamosfacil.exception.EmailDuplicadoException;
import com.prestamosfacil.exception.SolicitudInvalidaException;
import com.prestamosfacil.exception.TipoDocumentoNoEncontradoException;
import com.prestamosfacil.exception.UsuarioNoEncontradoException;
import com.prestamosfacil.model.TipoDocumento;
import com.prestamosfacil.model.Usuario;
import com.prestamosfacil.ports.INotificacionPublisherPort;
import com.prestamosfacil.ports.ITipoDocumentoPersistencePort;
import com.prestamosfacil.ports.IUsuarioPersistencePort;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.prestamosfacil.application.mocks.PrestamoFacilMocks.getMockUsuarioAnalista;
import static com.prestamosfacil.application.mocks.PrestamoFacilMocks.getMockUsuarioCliente;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioUseCaseTest {

    @Mock
    private IUsuarioPersistencePort usuarioPersistencePort;

    @Mock
    private ITipoDocumentoPersistencePort tipoDocumentoPersistencePort;

    @Mock
    private INotificacionPublisherPort notificacionPublisherPort;

    @InjectMocks
    private UsuarioUseCase usuarioUseCase;

    private final TipoDocumento tipoDocumento = new TipoDocumento(1L, "CC", "Cedula", true);

    @Test
    void registrarUsuarioGuardaConRolClienteYRetornaElUsuario() {
        when(tipoDocumentoPersistencePort.buscarPorId(1L)).thenReturn(Optional.of(tipoDocumento));
        when(usuarioPersistencePort.existeTipoDocumentoYNumeroDocumento(1L, "123")).thenReturn(false);
        when(usuarioPersistencePort.existeEmail("juan@mail.com")).thenReturn(false);

        Usuario usuarioGuardado = getMockUsuarioCliente();

        when(usuarioPersistencePort.guardar(any())).thenReturn(usuarioGuardado);

        Usuario resultado = usuarioUseCase.registrarUsuario("Juan", "Perez", "juan@mail.com", 1L, "123",
                BigDecimal.valueOf(2_000_000), "Clave123!");

        assertEquals(usuarioGuardado, resultado);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioPersistencePort).guardar(captor.capture());
        assertEquals(RolUsuario.CLIENTE, captor.getValue().rol());
        verify(notificacionPublisherPort, times(1)).publicarUsuarioRegistrado(any());
    }

    @Test
    void registrarUsuarioConEmailYaRegistradoLanzaExcepcion() {
        when(tipoDocumentoPersistencePort.buscarPorId(1L)).thenReturn(Optional.of(tipoDocumento));
        when(usuarioPersistencePort.existeTipoDocumentoYNumeroDocumento(1L, "123")).thenReturn(false);
        when(usuarioPersistencePort.existeEmail("juan@mail.com")).thenReturn(true);

        assertThrows(EmailDuplicadoException.class, () -> usuarioUseCase.registrarUsuario("Juan", "Perez",
                "juan@mail.com", 1L, "123", BigDecimal.valueOf(2_000_000), "Clave123!"));

        verify(usuarioPersistencePort, never()).guardar(any());
    }

    @Test
    void registrarUsuarioConTipoDocumentoInexistenteLanzaExcepcion() {
        when(tipoDocumentoPersistencePort.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThrows(TipoDocumentoNoEncontradoException.class, () -> usuarioUseCase.registrarUsuario("Juan", "Perez",
                "juan@mail.com", 99L, "123", BigDecimal.valueOf(2_000_000), "Clave123!"));

        verify(usuarioPersistencePort, never()).guardar(any());
    }

    @Test
    void registrarUsuarioConSalarioFueraDeRangoLanzaExcepcion() {
        when(tipoDocumentoPersistencePort.buscarPorId(1L)).thenReturn(Optional.of(tipoDocumento));
        when(usuarioPersistencePort.existeTipoDocumentoYNumeroDocumento(1L, "123")).thenReturn(false);
        when(usuarioPersistencePort.existeEmail("juan@mail.com")).thenReturn(false);

        assertThrows(SolicitudInvalidaException.class, () -> usuarioUseCase.registrarUsuario("Juan", "Perez",
                "juan@mail.com", 1L, "123", BigDecimal.valueOf(20_000_000), "Clave123!"));

        verify(usuarioPersistencePort, never()).guardar(any());
    }

    @Test
    void registrarAnalistaGuardaConRolAnalista() {
        when(tipoDocumentoPersistencePort.buscarPorId(1L)).thenReturn(Optional.of(tipoDocumento));
        when(usuarioPersistencePort.existeTipoDocumentoYNumeroDocumento(1L, "456")).thenReturn(false);
        when(usuarioPersistencePort.existeEmail("ana@mail.com")).thenReturn(false);

        Usuario analistaGuardado = getMockUsuarioAnalista();

        when(usuarioPersistencePort.guardar(any())).thenReturn(analistaGuardado);

        Usuario resultado = usuarioUseCase.registrarAnalista("Ana", "Torres", "ana@mail.com", 1L, "456",
                BigDecimal.valueOf(3_000_000), "Clave123!");

        assertEquals(analistaGuardado, resultado);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioPersistencePort).guardar(captor.capture());
        assertEquals(RolUsuario.ANALISTA, captor.getValue().rol());
    }

    @Test
    void buscarPorIdRetornaElUsuarioCuandoExiste() {
        Usuario usuario = getMockUsuarioCliente();

        when(usuarioPersistencePort.buscarPorId(5L)).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioUseCase.buscarPorId(5L);

        assertEquals(usuario, resultado);
    }

    @Test
    void buscarPorIdLanzaExcepcionCuandoNoExiste() {
        when(usuarioPersistencePort.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class, () -> usuarioUseCase.buscarPorId(99L));
    }

    @Test
    void buscarPorTipoDocumentoYNumeroDocumentoRetornaElUsuarioCuandoExiste() {
        Usuario usuario = getMockUsuarioCliente();
        when(usuarioPersistencePort.buscarPorTipoDocumentoYNumeroDocumento(1L, "123"))
                .thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioUseCase.buscarPorTipoDocumentoYNumeroDocumento(1L, "123");

        assertEquals(usuario, resultado);
    }

    @Test
    void buscarPorTipoDocumentoYNumeroDocumentoLanzaExcepcionCuandoNoExiste() {
        when(usuarioPersistencePort.buscarPorTipoDocumentoYNumeroDocumento(1L, "999"))
                .thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class,
                () -> usuarioUseCase.buscarPorTipoDocumentoYNumeroDocumento(1L, "999"));
    }

    @Test
    void listarClientesDelegaEnElPersistencePortConRolCliente() {
        Usuario cliente = getMockUsuarioCliente();
        when(usuarioPersistencePort.listarPorRol(RolUsuario.CLIENTE)).thenReturn(List.of(cliente));

        List<Usuario> resultado = usuarioUseCase.listarClientes();

        assertEquals(List.of(cliente), resultado);
        verify(usuarioPersistencePort).listarPorRol(RolUsuario.CLIENTE);
    }

    @Test
    void listarAnalistasDelegaEnElPersistencePortConRolAnalista() {
        Usuario analista = getMockUsuarioAnalista();
        when(usuarioPersistencePort.listarPorRol(RolUsuario.ANALISTA)).thenReturn(List.of(analista));

        List<Usuario> resultado = usuarioUseCase.listarAnalistas();

        assertEquals(List.of(analista), resultado);
        verify(usuarioPersistencePort).listarPorRol(RolUsuario.ANALISTA);
    }
}
