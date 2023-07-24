package com.xpresspayment.javabackendexercise.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(@Email(message = "provided email is invalid") String email, @NotBlank(message = "password cannot be blank") String password) {
}
