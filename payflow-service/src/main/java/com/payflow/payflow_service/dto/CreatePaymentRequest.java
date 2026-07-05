package com.payflow.payflow_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CreatePaymentRequest(
    @NotBlank(message = "Sender account is required")
    String senderAccount,

    @NotBlank(message = "Receiver account is required")
    String receiverAccount,

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    BigDecimal amount,

    @NotBlank(message = "Currency is required")
    String currency
) {}
