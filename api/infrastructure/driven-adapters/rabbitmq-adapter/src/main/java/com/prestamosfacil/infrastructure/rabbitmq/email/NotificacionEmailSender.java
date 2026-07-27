package com.prestamosfacil.infrastructure.rabbitmq.email;

import com.prestamosfacil.infrastructure.rabbitmq.pdf.GeneradorPdfPlanPagos;
import com.prestamosfacil.model.NotificacionRegistroEvento;
import com.prestamosfacil.model.NotificacionSolicitudEvento;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class NotificacionEmailSender {

    private static final String CORREO_APROBADO_ASUNTO = "¡Tu préstamo fue aprobado!";
    private static final String CORREO_RECHAZADO_ASUNTO = "Resultado de tu solicitud de préstamo";
    private static final String CORREO_BIENVENIDA_ASUNTO = "¡Registro exitoso en Préstamos Fácil!";
    private static final String LOGO_PATH = "templates/images/logo.png";
    private static final String REMITENTE = "noreply@prestamosfacil.com";

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final GeneradorPdfPlanPagos generadorPdfPlanPagos;

    public NotificacionEmailSender(JavaMailSender javaMailSender, TemplateEngine templateEngine,
                                    GeneradorPdfPlanPagos generadorPdfPlanPagos) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.generadorPdfPlanPagos = generadorPdfPlanPagos;
    }

    public void enviarAprobado(NotificacionSolicitudEvento evento) throws MessagingException {
        Context context = new Context();
        context.setVariable("nombreUsuario", evento.usuarioNombreCompleto());
        context.setVariable("tipoPrestamoNombre", evento.tipoPrestamoNombre());

        String html = templateEngine.process("correo-aprobado", context);

        byte[] pdf = generadorPdfPlanPagos.generar(evento.usuarioNombreCompleto(), evento.tipoPrestamoNombre(),
                evento.montoAprobado(), evento.cuotaMensual(), evento.planPagos());

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(REMITENTE);
        helper.setTo(evento.usuarioEmail());
        helper.setSubject(CORREO_APROBADO_ASUNTO);
        helper.setText(html, true);
        helper.addInline("logo", new ClassPathResource(LOGO_PATH));
        helper.addAttachment("plan-de-pagos.pdf", new ByteArrayResource(pdf));

        javaMailSender.send(mimeMessage);
    }

    public void enviarRechazado(NotificacionSolicitudEvento evento) throws MessagingException {
        Context context = new Context();
        context.setVariable("nombreUsuario", evento.usuarioNombreCompleto());
        context.setVariable("tipoPrestamoNombre", evento.tipoPrestamoNombre());

        String html = templateEngine.process("correo-rechazado", context);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(REMITENTE);
        helper.setTo(evento.usuarioEmail());
        helper.setSubject(CORREO_RECHAZADO_ASUNTO);
        helper.setText(html, true);
        helper.addInline("logo", new ClassPathResource(LOGO_PATH));

        javaMailSender.send(mimeMessage);
    }

    public void enviarBienvenida(NotificacionRegistroEvento evento) throws MessagingException {
        Context context = new Context();
        context.setVariable("nombreUsuario", evento.usuarioNombreCompleto());

        String html = templateEngine.process("correo-bienvenida", context);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(REMITENTE);
        helper.setTo(evento.usuarioEmail());
        helper.setSubject(CORREO_BIENVENIDA_ASUNTO);
        helper.setText(html, true);
        helper.addInline("logo", new ClassPathResource(LOGO_PATH));

        javaMailSender.send(mimeMessage);
    }
}
