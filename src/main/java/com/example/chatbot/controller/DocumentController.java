package com.example.chatbot.controller;

import com.example.chatbot.service.PdfIngestionService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/docs")
@RequiredArgsConstructor
public class DocumentController {

    private final PdfIngestionService pdfIngestionService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadDocument(
            @RequestParam("file") MultipartFile file) throws IOException {

        int chunks = pdfIngestionService.ingestPdf(file.getBytes());

        return ResponseEntity.ok(
                "Document ingested successfully. Chunks created: " + chunks
        );
    }
}
