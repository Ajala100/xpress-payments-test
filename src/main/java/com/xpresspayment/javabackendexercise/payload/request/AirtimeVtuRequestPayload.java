package com.xpresspayment.javabackendexercise.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AirtimeVtuRequestPayload {
    private String requestId;
    private String uniqueCode;
    private AirtimeVtuRequestDetails details;
}
