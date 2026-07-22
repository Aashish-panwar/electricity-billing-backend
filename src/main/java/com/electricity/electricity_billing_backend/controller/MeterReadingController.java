package com.electricity.electricity_billing_backend.controller;

import com.electricity.electricity_billing_backend.dto.request.MeterReadingRequest;
import com.electricity.electricity_billing_backend.dto.response.MeterReadingResponse;
import com.electricity.electricity_billing_backend.service.MeterReadingService;
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
@RequestMapping("/api/meter-readings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Meter Reading")
public class MeterReadingController {

    private final MeterReadingService meterReadingService;

    @Operation(summary = "Add Meter Reading")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<MeterReadingResponse> addReading(
            @Valid @RequestBody MeterReadingRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(meterReadingService.addReading(request));
    }

    @Operation(summary = "Get All Meter Readings")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<MeterReadingResponse>> getAllReadings() {

        return ResponseEntity.ok(
                meterReadingService.getAllReadings()
        );
    }

    @Operation(summary = "Get Meter Reading By Id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CONSUMER')")
    public ResponseEntity<MeterReadingResponse> getReadingById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                meterReadingService.getReadingById(id)
        );
    }

    @Operation(summary = "Get Readings By Meter")
    @GetMapping("/meter/{meterId}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CONSUMER')")
    public ResponseEntity<List<MeterReadingResponse>> getReadingsByMeter(
            @PathVariable Long meterId) {

        return ResponseEntity.ok(
                meterReadingService.getReadingsByMeter(meterId)
        );
    }

    @Operation(summary = "Update Meter Reading")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<MeterReadingResponse> updateReading(
            @PathVariable Long id,
            @Valid @RequestBody MeterReadingRequest request) {

        return ResponseEntity.ok(
                meterReadingService.updateReading(id, request)
        );
    }

    @Operation(summary = "Delete Meter Reading")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteReading(
            @PathVariable Long id) {

        meterReadingService.deleteReading(id);

        return ResponseEntity.ok("Meter reading deleted successfully.");
    }
}