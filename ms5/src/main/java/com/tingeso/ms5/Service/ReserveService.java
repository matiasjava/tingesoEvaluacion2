package com.tingeso.ms5.Service;

import com.tingeso.ms5.Config.RestTemplateConfig;
import com.tingeso.ms5.DTOs.*;
import com.tingeso.ms5.Entity.*;
import com.tingeso.ms5.Repository.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.wp.usermodel.Paragraph;
import org.glassfish.jersey.message.internal.DataSourceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;


import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.swing.text.Document;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;



import java.util.ArrayList;
import java.util.List;

@Service
public class ReserveService {

    @Autowired
    private ReserveRepository reserveRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReserveDetailsRepository detailsRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender mailSender;

    public ReserveEntity processReservation(ReserveRequestDTO dto) {
        ReserveEntity reserve = new ReserveEntity();
        reserve.setFechaUso(dto.getFechaUso());
        reserve.setHoraInicio(dto.getHoraInicio());
        reserve.setHoraFin(dto.getHoraFin());

        UserEntity user = userRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        reserve.setCliente(user);
        reserve.setVueltasOTiempo(dto.getVueltasOTiempo());
        reserve.setCantidadPersonas(dto.getMiembros().size());

        List<ReserveDetailsEntity> detalles = new ArrayList<>();
        double montoFinalTotal = 0;
        int cumpleanosAplicados = 0;
        int maxCumpleanos = calcularMaxCumpleanos(dto.getMiembros().size());

        for (MemberDTO miembro : dto.getMiembros()) {
            ReserveDetailsEntity detalle = new ReserveDetailsEntity();
            detalle.setMemberName(miembro.getNombre());
            detalle.setDateBirthday(miembro.getFechaNacimiento());
            detalle.setUserId(miembro.getUserId());
            detalle.setReserve(reserve);

            double montoBase = obtenerTarifa(dto.getVueltasOTiempo());
            double descuentoCantidad = obtenerDescuentoPorCantidad(dto.getMiembros().size());
            double descuentoFrecuencia = obtenerDescuentoPorCategoria(miembro.getRut());

            double descuentoCumple = 0;
            if (cumpleanosAplicados < maxCumpleanos &&
                    miembro.getFechaNacimiento().equals(dto.getFechaUso())) {
                descuentoCumple = 0.50;
                cumpleanosAplicados++;
            }

            double descuentoFinal = Math.max(descuentoCumple,
                    Math.max(descuentoFrecuencia, descuentoCantidad));

            double montoConDescuento = montoBase * (1 - descuentoFinal);

            detalle.setDiscount(descuentoFinal);
            detalle.setMontoFinal(montoConDescuento);

            detalles.add(detalle);
            montoFinalTotal += montoConDescuento;
        }

        reserve.setMontoFinal(montoFinalTotal);
        reserve.setDetalles(detalles);

        ReserveEntity reservaGuardada = reserveRepository.save(reserve);

        try {
            byte[] comprobantePdf = generarComprobantePdf(reservaGuardada);

            String[] destinatarios = reservaGuardada.getDetalles().stream()
                    .map(det -> userService.findUserById(dto.getClienteId()).getEmail())
                    .toArray(String[]::new);

            enviarComprobantePorCorreo(destinatarios, comprobantePdf, reservaGuardada.getCodigoReserva());
            System.out.println(userService.findUserById(dto.getClienteId()).getEmail());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al generar o enviar el comprobante.");
        }

        return reservaGuardada;
    }

    public byte[] generarComprobantePdf(ReserveEntity reserve) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        contentStream.setFont(PDType1Font.HELVETICA, 12);
        float yPosition = 750; // posicion eje Y
        float margin = 50; // margen
        float lineHeight = 15; // altura

        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
        contentStream.showText("Comprobante de Reserva");
        contentStream.endText();

        yPosition -= lineHeight;

        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.showText("Código de Reserva: " + reserve.getCodigoReserva());
        contentStream.newLineAtOffset(0, -lineHeight);
        contentStream.showText("Fecha y Hora de la Reserva: " + reserve.getFechaUso() + " " + reserve.getHoraInicio() + " - " + reserve.getHoraFin());
        contentStream.newLineAtOffset(0, -lineHeight);
        contentStream.showText("Número de Vueltas o Tiempo Máximo: " + reserve.getVueltasOTiempo());
        contentStream.newLineAtOffset(0, -lineHeight);
        contentStream.showText("Cantidad de Personas: " + reserve.getCantidadPersonas());
        contentStream.newLineAtOffset(0, -lineHeight);
        contentStream.showText("Nombre del Cliente: " + reserve.getDetalles().get(0).getMemberName());
        contentStream.endText();

        yPosition -= (lineHeight * 5);

        // detalles tablita
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.showText("Detalle de Pago:");
        contentStream.endText();

        yPosition -= lineHeight;

        // encabezados
        float tableWidth = 500;
        float[] columnWidths = {100, 80, 80, 80, 80, 80};
        String[] headers = {"Nombre", "Tarifa Base", "Descuento", "Monto Final", "IVA", "Total con IVA"};

        double totalReservaConIva = 0.0;

        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
        float xPosition = margin;
        for (String header : headers) {
            contentStream.beginText();
            contentStream.newLineAtOffset(xPosition, yPosition);
            contentStream.showText(header);
            contentStream.endText();
            xPosition += columnWidths[headers.length - headers.length + 1];
        }
        yPosition -= lineHeight;

        contentStream.setFont(PDType1Font.HELVETICA, 10);
        for (ReserveDetailsEntity detalle : reserve.getDetalles()) {
            double tarifaBase = detalle.getMontoFinal() / (1 - detalle.getDiscount());
            double iva = detalle.getMontoFinal() * 0.19; //IVA
            double totalConIva = detalle.getMontoFinal() + iva;

            totalReservaConIva += totalConIva; // Sumar el total con IVA

            xPosition = margin;
            String[] row = {
                    detalle.getMemberName(),
                    String.format("%.2f", tarifaBase),
                    String.format("%.2f", 100 * detalle.getDiscount()) + " %",
                    String.format("%.2f", detalle.getMontoFinal()),
                    String.format("%.2f", iva),
                    String.format("%.2f", totalConIva)
            };

            for (String cell : row) {
                contentStream.beginText();
                contentStream.newLineAtOffset(xPosition, yPosition);
                contentStream.showText(cell);
                contentStream.endText();
                xPosition += columnWidths[row.length - row.length + 1];
            }

            yPosition -= lineHeight;

            // para agregar otra pagina
            if (yPosition < 50) {
                contentStream.close();
                page = new PDPage();
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                yPosition = 750;
            }
        }
        // total de la reserva al final
        yPosition -= lineHeight;
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.showText("Total Reserva: " + "$" + String.format("%.2f", totalReservaConIva));
        contentStream.endText();

        contentStream.close();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();

        return outputStream.toByteArray();
    }

    public void enviarComprobantePorCorreo(String[] destinatarios, byte[] pdfBytes, String reserveCode) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(destinatarios);
        helper.setSubject("Comprobante de Reserva " + reserveCode);
        helper.setText("Hola");

        helper.addAttachment("comprobante.pdf", new ByteArrayDataSource(pdfBytes, "application/pdf"));

        mailSender.send(message);
    }

    public int obtenerTarifa(int duracionMinutos) {
        String url = "http://localhost:8001/api/tarifas/duracion/" + duracionMinutos;
        TariffDTO tarifa = restTemplate.getForObject(url, TariffDTO.class);
        if (tarifa == null) {
            throw new RuntimeException("No se encontró una tarifa para la duración: " + duracionMinutos);
        }
        return tarifa.getPrecio();
    }

    public double obtenerDescuentoPorCantidad(int cantidadPersonas) {
        String url = "http://localhost:8002/api/descuentoscantidad/" + cantidadPersonas;
        DescuentoCantidadResponse response = restTemplate.getForObject(url, DescuentoCantidadResponse.class);
        Double descuento = response.getDescuento();

        return descuento;

    }


    public double obtenerDescuentoPorCategoria(String rut) {
        System.out.println(rut);
        int visitas = userService.getUserByRut(rut).getNumberVisits();
        String url = "http://localhost:8003/api/frecuencia/" + visitas;
        DiscountFrecuenciaEntity response = restTemplate.getForObject(url, DiscountFrecuenciaEntity.class);

        if (response == null || response.getDescuento() == null) {
            throw new RuntimeException("No se pudo obtener el descuento para el usuario con ID: " + userService.getUserByRut(rut).getId());
        }

        return response.getDescuento();
    }


    public int calcularMaxCumpleanos(int cantidadPersonas) {
        if (cantidadPersonas >= 6 && cantidadPersonas <= 10) {
            return 2;
        } else if (cantidadPersonas >= 3 && cantidadPersonas <= 5) {
            return 1;
        } else {
            return 0;
        }
    }




}