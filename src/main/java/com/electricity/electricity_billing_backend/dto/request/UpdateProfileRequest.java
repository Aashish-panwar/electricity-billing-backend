package com.electricity.electricity_billing_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {

    @NotBlank
    private String fullName;

    @NotBlank
    private String mobileNumber;

}