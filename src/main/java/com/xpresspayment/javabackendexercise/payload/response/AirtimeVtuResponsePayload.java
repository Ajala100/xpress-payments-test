package com.xpresspayment.javabackendexercise.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class AirtimeVtuResponsePayload {
    private String requestId;
    private String referenceId;
    private String responseCode;
    private String responseMessage;
    private Object data;
}
