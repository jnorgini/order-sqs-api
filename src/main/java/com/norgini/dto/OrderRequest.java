package com.norgini.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderRequest(@NotNull Long clientId, @NotNull @Positive Double amount) {
}
