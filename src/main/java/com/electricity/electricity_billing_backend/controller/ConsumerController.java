package com.electricity.electricity_billing_backend.controller;

import com.electricity.electricity_billing_backend.dto.request.ConsumerRequest;
import com.electricity.electricity_billing_backend.dto.response.ConsumerResponse;
import com.electricity.electricity_billing_backend.service.ConsumerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consumers")
@RequiredArgsConstructor
@Tag(
        name = "Consumer Management",
        description = "APIs for managing electricity consumers"
)
public class ConsumerController {

    private final ConsumerService consumerService;

    @Operation(summary = "Create Consumer")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<ConsumerResponse> createConsumer(
            @Valid @RequestBody ConsumerRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(consumerService.createConsumer(request));
    }

    @Operation(summary = "Get Consumer By Id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CONSUMER')")
    public ResponseEntity<ConsumerResponse> getConsumerById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                consumerService.getConsumerById(id)
        );
    }

    @Operation(summary = "Get All Consumers")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<ConsumerResponse>> getAllConsumers() {

        return ResponseEntity.ok(
                consumerService.getAllConsumers()
        );
    }

    @Operation(summary = "Get Consumers With Pagination")
    @GetMapping("/page")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<Page<ConsumerResponse>> getAllConsumers(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "id") String sortBy,

            @RequestParam(defaultValue = "asc") String direction
    ) {

        return ResponseEntity.ok(
                consumerService.getAllConsumers(
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }

    @Operation(summary = "Update Consumer")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<ConsumerResponse> updateConsumer(
            @PathVariable Long id,
            @Valid @RequestBody ConsumerRequest request) {

        return ResponseEntity.ok(
                consumerService.updateConsumer(id, request)
        );
    }

    @Operation(summary = "Delete Consumer")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteConsumer(
            @PathVariable Long id) {

        consumerService.deleteConsumer(id);

        return ResponseEntity.ok("Consumer deleted successfully.");
    }

    @Operation(summary = "Search Consumers")
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<Page<ConsumerResponse>> search(

            @RequestParam String keyword,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(

                consumerService.searchConsumers(
                        keyword,
                        page,
                        size
                )
        );
    }

}
