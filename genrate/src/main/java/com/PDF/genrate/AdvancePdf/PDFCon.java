package com.PDF.genrate.AdvancePdf;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;

import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;

import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api")
public class PDFCon {

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> downloadPdf() {
        try {

//            String dest = "blurred_text.pdf";
//            PdfWriter writer = new PdfWriter(new File(dest));
//            PdfDocument pdfDocument = new PdfDocument(writer);
//            Document document = new Document(pdfDocument, PageSize.A4);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PdfWriter pdfWriter = new PdfWriter(byteArrayOutputStream);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdfDocument);

            // Custom Font
            PdfFont font = PdfFontFactory.createFont("Helvetica-Bold");

            // Adding an Image
            String imagePath = "C:/Users/7475/Pictures/web coding/download (6).jpeg";  // Change the path as needed
            if (Files.exists(Paths.get(imagePath))) {
                ImageData imageData = ImageDataFactory.create(Files.readAllBytes(Paths.get(imagePath)));
                Image logo = new Image(imageData).scaleToFit(100, 100).setTextAlignment(TextAlignment.CENTER);
                document.add(logo);
            }

            // Title with Custom Font and Color
            Color titleColor = new DeviceRgb(0, 102, 204);
            Paragraph title = new Paragraph("Custom PDF Report")
                    .setFont(font)
                    .setFontSize(20)
                    .setFontColor(titleColor)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(title);

            // Custom Styled Paragraph with Borders
            Paragraph paragraph = new Paragraph("This is a sample paragraph with a custom border, font size, and color.")
                    .setFont(font)
                    .setFontSize(14)
                    .setFontColor(new DeviceRgb(0, 153, 76))  // Green color
                    .setBorder(new SolidBorder(1))
                    .setPadding(10)
                    .setTextAlignment(TextAlignment.JUSTIFIED);
            document.add(paragraph);

            // **Adding a Table**
            float[] columnWidths = {100f, 150f, 100f}; // Define column widths
            Table table = new Table(columnWidths);

            // **Header Row**
            table.addHeaderCell(new Cell().add(new Paragraph("ID").setBold().setFontColor(new DeviceRgb(255, 255, 255)))
                    .setBackgroundColor(new DeviceRgb(0, 102, 204))
                    .setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Name").setBold().setFontColor(new DeviceRgb(255, 255, 255)))
                    .setBackgroundColor(new DeviceRgb(0, 102, 204))
                    .setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Price").setBold().setFontColor(new DeviceRgb(255, 255, 255)))
                    .setBackgroundColor(new DeviceRgb(0, 102, 204))
                    .setTextAlignment(TextAlignment.CENTER));

            // **Adding Rows (Dynamic Data)**
            String[][] data = {
                    {"1", "Java Book", "$29.99"},
                    {"2", "Spring Boot Book", "$35.50"},
                    {"3", "Hibernate Guide", "$40.00"}
            };

            for (String[] row : data) {
                for (String cellData : row) {
                    table.addCell(new Cell().add(new Paragraph(cellData)).setTextAlignment(TextAlignment.CENTER));
                }
            }

            document.add(table);

            ImageData watermarkImage = ImageDataFactory.create("C:/Users/7475/Pictures/web coding/download (4).jpeg");
            Image watermark = new Image(watermarkImage).setFixedPosition(200, 500).setOpacity(0.2f);
            document.add(watermark);

            // Load Image
            String imagePath12 = "C:/Users/7475/Pictures/web coding/download (5).jpeg";  // Change the path
            if (Files.exists(Paths.get(imagePath12))) {
                ImageData imageData = ImageDataFactory.create(Files.readAllBytes(Paths.get(imagePath12)));
                Image backgroundImage = new Image(imageData);

                // Scale Image to Fit the Page
                backgroundImage.scaleAbsolute(PageSize.A4.getWidth(), PageSize.A4.getHeight());
                backgroundImage.setFixedPosition(0, 0);
                backgroundImage.setOpacity(0.15f); // Reduce opacity to create a blur effect

                // Add to Background
                document.add(backgroundImage);
            }

            // Sample Text Over the Background
            document.add(new Paragraph("This is a sample PDF with a blurred background logo.")
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(300));

            String text = "This is blurred text!";

            // Fake blur using multiple layers of text with different opacities & shifts
            for (float i = 0.2f; i <= 1; i += 0.2f) {
                document.add(new Paragraph(text)
                        .setFontSize(40)
                        .setFontColor(new DeviceRgb(100, 100, 100)) // Grayish color
                        .setOpacity(i - 0.2f) // Reduce opacity for each layer
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFixedPosition(150 + i * 2, 500 - i * 2, 400) // Shifted position
                );
            }

            // Add main clear text on top
            document.add(new Paragraph(text)
                    .setFontSize(40)
                    .setFontColor(ColorConstants.BLACK)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFixedPosition(150, 500, 400)
            );

//            Adding Page Numbers
            int totalPages = pdfDocument.getNumberOfPages();
            for (int i = 1; i <= totalPages; i++) {
                document.showTextAligned(new Paragraph("Page " + i + " of " + totalPages),
                        550, 20, i, TextAlignment.RIGHT, VerticalAlignment.BOTTOM, 0);
            }

//
//            Text Watermark
            PdfCanvas canvas = new PdfCanvas(pdfDocument.getFirstPage());
            canvas.setFontAndSize(PdfFontFactory.createFont(), 60);
            canvas.setStrokeColor(new DeviceRgb(200, 200, 200));
            canvas.beginText()
                    .setTextMatrix(30, 400)
                    .showText("CONFIDENTIAL")
                    .endText();

            // **Create Paragraph for Date & Time**
            String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            Paragraph dateTimeParagraph = new Paragraph("Generated on: " + currentDateTime)
                    .setFont(font)                    // Custom Font
                    .setFontSize(14)                  // Font Size
                    .setFontColor(new DeviceRgb(0, 102, 204)) // Font Color (Blue)
                    .setTextAlignment(TextAlignment.RIGHT) // Align to Right
                    .setMarginBottom(20);

            // **Add to Document**
            document.add(dateTimeParagraph);


            // Closing the Document
            document.close();

            // Return PDF as a downloadable file
            byte[] pdfBytes = byteArrayOutputStream.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=custom.pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
            System.out.println("PDF created with blurred background image.");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
