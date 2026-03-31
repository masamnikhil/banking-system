package com.bankingsys.account_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequest {

    @NotBlank(message = "User ID is required")
    private String userId;
    @NotBlank(message = "Account type is required")
    @Pattern(
            regexp = "SAVINGS|CURRENT",
            message = "Account type must be SAVINGS or CURRENT"
    )
    private String accountType;
    @NotBlank(message = "Branch name is required")
    private String branchName;

}
