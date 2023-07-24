package com.xpresspayment.javabackendexercise.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class BaseResponsePayload {
    private boolean isSuccessful;
    private String message;
    private int responseCode;
    private LocalDate timestamp;
}
