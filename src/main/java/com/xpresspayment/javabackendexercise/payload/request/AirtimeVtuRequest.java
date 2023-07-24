package com.xpresspayment.javabackendexercise.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record AirtimeVtuRequest(@NotBlank(message = "phone number cannot be blank") String phoneNumber, @Positive(message = "Invalid airtime amount") int amount, @NotBlank(message = "Unique code cannot be blank") String uniqueCode ) {
}
