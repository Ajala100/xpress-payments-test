package com.xpresspayment.javabackendexercise.security.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthenticationResponsePayload {
    private boolean isSuccessful;
    private String message;
    private int responseCode;
    private String timeStamp;
}
