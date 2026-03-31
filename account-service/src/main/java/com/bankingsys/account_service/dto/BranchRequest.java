package com.bankingsys.account_service.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BranchRequest {

    @NotBlank(message = "Branch code is required")
    @Pattern(regexp = "\\d{5}", message = "Branch code must be 5 digits")
    private String branchCode;

    @NotBlank(message = "Branch name is required")
    @Size(min = 3, max = 50, message = "Branch name must be between 3 and 50 characters")
    private String branchName;

    @NotBlank(message = "City is required")
    @Size(min = 2, max = 40, message = "City must be between 2 and 40 characters")
    private String city;

    @NotBlank(message = "IFSC code is required")
    @Pattern(
            regexp = "^[A-Z]{4}0[A-Z0-9]{6}$",
            message = "Invalid IFSC code format"
    )
    private String ifscCode;
}
