package com.bankingsys.account_service.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest {

    @NotBlank(message = "From account is required")
    @Pattern(regexp = "\\d{11}", message = "From account must be 11 digits")
    private String fromAccount;

    @NotBlank(message = "To account is required")
    @Pattern(regexp = "\\d{11}", message = "To account must be 11 digits")
    private String toAccount;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Digits(integer = 12, fraction = 2, message = "Invalid amount format")
    private BigDecimal amount;

    @NotBlank(message = "Profile password is required")
    private String profilePassword;
}
