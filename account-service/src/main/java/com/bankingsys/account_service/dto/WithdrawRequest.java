package com.bankingsys.account_service.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawRequest {

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Digits(integer = 12, fraction = 2, message = "Invalid amount format")
    private BigDecimal amount;

    @NotBlank(message = "Account number is required")
    @Pattern(regexp = "\\d{11}", message = "Account number must be 11 digits")
    private String accountNumber;

    @NotBlank(message = "Profile password is required")
    private String profilePassword;
}
