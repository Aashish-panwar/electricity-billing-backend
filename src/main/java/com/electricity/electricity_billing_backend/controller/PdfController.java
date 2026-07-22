package com.electricity.electricity_billing_backend.controller;


import com.electricity.electricity_billing_backend.service.PdfService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/pdf")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PdfController {


    private final PdfService pdfService;



    @GetMapping("/bill/{billId}")
    public ResponseEntity<byte[]> downloadBillPdf(
            @PathVariable Long billId) {


        byte[] pdf =
                pdfService.generateBillPdf(
                        billId
                );


        return ResponseEntity.ok()

                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=electricity-bill.pdf"
                )

                .contentType(
                        MediaType.APPLICATION_PDF
                )

                .body(pdf);

    }

}