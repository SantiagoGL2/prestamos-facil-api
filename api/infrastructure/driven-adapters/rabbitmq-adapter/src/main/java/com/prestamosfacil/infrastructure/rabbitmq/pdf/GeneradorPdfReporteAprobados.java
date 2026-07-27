package com.prestamosfacil.infrastructure.rabbitmq.pdf;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.prestamosfacil.model.ReportePrestamoAprobado;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;

@Component
public class GeneradorPdfReporteAprobados {

    private final TemplateEngine templateEngine;

    public GeneradorPdfReporteAprobados(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generar(List<ReportePrestamoAprobado> prestamos, BigDecimal montoTotalAprobado) {
        Context context = new Context();
        context.setVariable("prestamos", prestamos);
        context.setVariable("montoTotalAprobado", montoTotalAprobado);
        context.setVariable("logoBase64", leerLogoBase64());

        String html = templateEngine.process("reporte-prestamos-aprobados", context);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.run();

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo generar el PDF del reporte de prestamos aprobados", e);
        }
    }

    private String leerLogoBase64() {
        try (InputStream inputStream = new ClassPathResource("templates/images/logo.png").getInputStream()) {
            return Base64.getEncoder().encodeToString(inputStream.readAllBytes());
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer el logo para el PDF", e);
        }
    }
}
