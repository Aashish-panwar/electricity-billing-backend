package com.electricity.electricity_billing_backend.controller;

import com.electricity.electricity_billing_backend.dto.request.PaymentRequest;
import com.electricity.electricity_billing_backend.dto.response.PaymentResponse;
import com.electricity.electricity_billing_backend.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Bill")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "Make Payment")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<PaymentResponse> makePayment(
            @Valid @RequestBody PaymentRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.makePayment(request));
    }


    @Operation(summary = "Get All Payments")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {

        return ResponseEntity.ok(
                paymentService.getAllPayments()
        );
    }

    @Operation(summary = "Get Payment By Id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<PaymentResponse> getPaymentById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                paymentService.getPaymentById(id)
        );
    }

    @Operation(summary = "Get Payment By Transaction Id")
    @GetMapping("/transaction/{transactionId}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<PaymentResponse> getPaymentByTransactionId(
            @PathVariable String transactionId) {

        return ResponseEntity.ok(
                paymentService.getPaymentByTransactionId(transactionId)
        );
    }

    @Operation(summary = "Get Payment By bill")
    @GetMapping("/bill/{billId}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CONSUMER')")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByBill(
            @PathVariable Long billId) {

        return ResponseEntity.ok(
                paymentService.getPaymentsByBill(billId)
        );
    }

    @Operation(summary = "Get Payments By Consumer")
    @GetMapping("/consumer/{consumerId}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByConsumer(
            @PathVariable Long consumerId) {

        return ResponseEntity.ok(
                paymentService.getPaymentsByConsumer(consumerId)
        );
    }

    @Operation(summary = "Delete Payment")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deletePayment(
            @PathVariable Long id) {

        paymentService.deletePayment(id);

        return ResponseEntity.ok(
                "Payment deleted successfully."
        );
    }

}