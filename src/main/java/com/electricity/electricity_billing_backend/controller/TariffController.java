package com.electricity.electricity_billing_backend.controller;

import com.electricity.electricity_billing_backend.dto.request.TariffRequest;
import com.electricity.electricity_billing_backend.dto.response.TariffResponse;
import com.electricity.electricity_billing_backend.service.TariffService;
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
@RequestMapping("/api/tariffs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Tariff")
public class TariffController {

    private final TariffService tariffService;

    @Operation(summary = "Create Tariff")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TariffResponse> createTariff(
            @Valid @RequestBody TariffRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tariffService.createTariff(request));
    }

    @Operation(summary = "Update Tariff")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TariffResponse> updateTariff(
            @PathVariable Long id,
            @Valid @RequestBody TariffRequest request) {

        return ResponseEntity.ok(
                tariffService.updateTariff(id, request));
    }

    @Operation(summary = "Get Tariff By Id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<TariffResponse> getTariff(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                tariffService.getTariffById(id));
    }

    @Operation(summary = "Get All Tariffs")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<TariffResponse>> getAllTariffs() {

        return ResponseEntity.ok(
                tariffService.getAllTariffs());
    }

    @Operation(summary = "Delete Tariff")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTariff(
            @PathVariable Long id) {

        tariffService.deleteTariff(id);

        return ResponseEntity.noContent().build();
    }
}