package com.innostore.improvementhub.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PdfGenerationService {

    public byte[] generateIdeaAnalysisPdf(String coreConcept, String problemOpportunity, String aiAnalysis) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Add title
            Paragraph title = new Paragraph("INNOSTORE - Idea Analysis Report")
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // Add date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            Paragraph date = new Paragraph("Generated: " + LocalDateTime.now().format(formatter))
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(date);

            // Add Core Concept
            Paragraph conceptHeader = new Paragraph("Core Concept")
                    .setFontSize(14)
                    .setBold()
                    .setMarginTop(20);
            document.add(conceptHeader);

            Paragraph conceptContent = new Paragraph(coreConcept)
                    .setFontSize(12)
                    .setMarginBottom(15);
            document.add(conceptContent);

            // Add Problem/Opportunity
            Paragraph problemHeader = new Paragraph("Problem/Opportunity")
                    .setFontSize(14)
                    .setBold()
                    .setMarginTop(10);
            document.add(problemHeader);

            Paragraph problemContent = new Paragraph(problemOpportunity)
                    .setFontSize(12)
                    .setMarginBottom(15);
            document.add(problemContent);

            // Add AI Analysis
            Paragraph analysisHeader = new Paragraph("AI Analysis")
                    .setFontSize(14)
                    .setBold()
                    .setMarginTop(10);
            document.add(analysisHeader);

            Paragraph analysisContent = new Paragraph(aiAnalysis)
                    .setFontSize(12)
                    .setMarginBottom(15);
            document.add(analysisContent);

            // Add footer
            Paragraph footer = new Paragraph("Thank you for using INNOSTORE!")
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(30);
            document.add(footer);

            document.close();

            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }
}
