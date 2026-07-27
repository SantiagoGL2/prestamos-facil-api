package com.prestamosfacil.application.adapter;

import com.prestamosfacil.application.port.IReportePort;
import com.prestamosfacil.enums.RolUsuario;
import com.prestamosfacil.exception.SolicitudInvalidaException;
import com.prestamosfacil.model.ReporteMontosAprobados;
import com.prestamosfacil.model.ReportePrestamoAprobado;
import com.prestamosfacil.model.ReporteSolicitadoEvento;
import com.prestamosfacil.model.Usuario;
import com.prestamosfacil.ports.IPrestamoPersistencePort;
import com.prestamosfacil.ports.IReportePublisherPort;
import com.prestamosfacil.ports.IUsuarioPersistencePort;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Genera el reporte global de préstamos aprobados y coordina su envío por correo. El envío no
 * va a un destinatario puntual: se resuelve la lista completa de usuarios con rol
 * {@code ANALISTA} y se publica un evento independiente por cada uno, para que el fallo de un
 * envío no afecte a los demás.
 */
@Service
public class ReporteUseCase implements IReportePort {

    private final IPrestamoPersistencePort prestamoPersistencePort;
    private final IUsuarioPersistencePort usuarioPersistencePort;
    private final IReportePublisherPort reportePublisherPort;

    public ReporteUseCase(IPrestamoPersistencePort prestamoPersistencePort,
                           IUsuarioPersistencePort usuarioPersistencePort,
                           IReportePublisherPort reportePublisherPort) {
        this.prestamoPersistencePort = prestamoPersistencePort;
        this.usuarioPersistencePort = usuarioPersistencePort;
        this.reportePublisherPort = reportePublisherPort;
    }

    @Override
    public ReporteMontosAprobados generarReporteMontosAprobados() {
        List<ReportePrestamoAprobado> prestamos = prestamoPersistencePort.listarAprobados();

        BigDecimal total = prestamos.stream()
                .map(ReportePrestamoAprobado::montoAprobado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ReporteMontosAprobados(prestamos, total);
    }

    @Override
    public void enviarReportePorCorreoAAnalistas() {
        ReporteMontosAprobados reporte = generarReporteMontosAprobados();
        List<Usuario> analistas = usuarioPersistencePort.listarPorRol(RolUsuario.ANALISTA);

        if (analistas.isEmpty()) {
            throw new SolicitudInvalidaException("No hay analistas registrados para enviar el reporte");
        }

        analistas.forEach(analista -> reportePublisherPort.publicarReporteParaEnvio(new ReporteSolicitadoEvento(
                analista.email(), analista.nombres() + " " + analista.apellidos(), reporte.prestamos(),
                reporte.montoTotalAprobado())));
    }
}
