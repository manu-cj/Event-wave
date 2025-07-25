package com.manu.template.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
public class TicketService {
    @Autowired
    private TemplateEngine templateEngine;

    public String generateTicketPdf(UUID reservationId, String username, String eventName) {
        try {
            // Générer le QR code en base64
            String qrContent = "https://localhost:4200/reservation/" + reservationId;            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 150, 150);
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "png", baos);
            String qrBase64 = java.util.Base64.getEncoder().encodeToString(baos.toByteArray());

            // Préparer le contexte Thymeleaf
            org.thymeleaf.context.Context context = new org.thymeleaf.context.Context();
            context.setVariable("reservationId", reservationId);
            context.setVariable("username", username);
            context.setVariable("eventName", eventName);
            context.setVariable("qrBase64", qrBase64);

            // Générer le HTML
            String htmlContent = templateEngine.process("ticket", context);

            // Convertir le HTML en PDF
            String dirPath = "tickets";
            File dir = new File(dirPath);
            if (!dir.exists()) dir.mkdirs();
            String filePath = dirPath + File.separator + "ticket_" + reservationId + ".pdf";
            try (FileOutputStream os = new FileOutputStream(filePath)) {
                com.openhtmltopdf.pdfboxout.PdfRendererBuilder builder = new com.openhtmltopdf.pdfboxout.PdfRendererBuilder();
                builder.withHtmlContent(htmlContent, null);
                builder.toStream(os);
                builder.run();
            }
            return filePath;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
    }

    public byte[] getTicketPdfBytes(String filePath) {
        try {
            return java.nio.file.Files.readAllBytes(new java.io.File(filePath).toPath());
        } catch (Exception e) {
            throw new RuntimeException("Error when reading PDF", e);
        }
    }
}
