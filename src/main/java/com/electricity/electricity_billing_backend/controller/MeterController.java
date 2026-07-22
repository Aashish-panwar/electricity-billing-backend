package com.electricity.electricity_billing_backend.controller;

import com.electricity.electricity_billing_backend.dto.request.MeterRequest;
import com.electricity.electricity_billing_backend.dto.response.MeterResponse;
import com.electricity.electricity_billing_backend.service.MeterService;
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
@RequestMapping("/api/meters")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Meter")
public class MeterController {

    private final MeterService meterService;

    @Operation(summary = "Create Meter")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<MeterResponse> createMeter(
            @Valid @RequestBody MeterRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(meterService.createMeter(request));
    }


    @Operation(summary = "Get All Meters")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<MeterResponse>> getAllMeters() {

        return ResponseEntity.ok(
                meterService.getAllMeters()
        );
    }

    @Operation(summary = "Get Meter By Id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CONSUMER')")
    public ResponseEntity<MeterResponse> getMeterById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                meterService.getMeterById(id)
        );
    }

    @Operation(summary = "Update Meter")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<MeterResponse> updateMeter(
            @PathVariable Long id,
            @Valid @RequestBody MeterRequest request) {

        return ResponseEntity.ok(
                meterService.updateMeter(id, request)
        );
    }

    @Operation(summary = "Delete Meter")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteMeter(
            @PathVariable Long id) {

        meterService.deleteMeter(id);

        return ResponseEntity.ok("Meter deleted successfully.");
    }
}