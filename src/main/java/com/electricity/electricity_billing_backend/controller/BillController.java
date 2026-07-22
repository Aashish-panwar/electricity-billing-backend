package com.electricity.electricity_billing_backend.controller;

import com.electricity.electricity_billing_backend.dto.request.BillRequest;
import com.electricity.electricity_billing_backend.dto.response.BillResponse;
import com.electricity.electricity_billing_backend.service.BillService;
import com.electricity.electricity_billing_backend.service.PdfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
@RestController
@RequestMapping("/api/bills")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Bill")
public class BillController {

    private final BillService billService;
    private final PdfService pdfService;

    @Operation(summary = "Generate Bill")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<BillResponse> generateBill(
            @Valid @RequestBody BillRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(billService.generateBill(request));
    }

    @Operation(summary = "Get All Bills")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<BillResponse>> getAllBills() {

        return ResponseEntity.ok(
                billService.getAllBills()
        );
    }

    @Operation(summary = "Get Bill By Id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CONSUMER')")
    public ResponseEntity<BillResponse> getBillById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                billService.getBillById(id)
        );
    }

    @Operation(summary = "Get Bill By Bill Number")
    @GetMapping("/number/{billNumber}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<BillResponse> getBillByNumber(
            @PathVariable String billNumber) {

        return ResponseEntity.ok(
                billService.getBillByNumber(billNumber)
        );
    }



    @Operation(summary = "Get Bills By Consumer")
    @GetMapping("/consumer/{consumerId}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CONSUMER')")
    public ResponseEntity<List<BillResponse>> getBillsByConsumer(
            @PathVariable Long consumerId) {

        return ResponseEntity.ok(
                billService.getBillsByConsumer(consumerId)
        );
    }

    @Operation(summary = "Delete Bill")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteBill(
            @PathVariable Long id) {

        billService.deleteBill(id);

        return ResponseEntity.ok(
                "Bill deleted successfully."
        );
    }

    @Operation(summary = "Download Bill PDF")
    @GetMapping("/{billId}/download")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CONSUMER')")
    public ResponseEntity<byte[]> downloadBillPdf(
            @PathVariable Long billId
    ) {

        byte[] pdf = pdfService.generateBillPdf(billId);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=bill-" + billId + ".pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

}