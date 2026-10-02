package com.example.chatbot.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PdfService {

    public String extractText(byte[] pdf) throws IOException {
        try (PDDocument document = Loader.loadPDF(pdf)){
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }
}
