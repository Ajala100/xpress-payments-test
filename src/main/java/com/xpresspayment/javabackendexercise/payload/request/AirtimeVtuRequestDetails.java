package com.xpresspayment.javabackendexercise.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AirtimeVtuRequestDetails {
    private String phoneNumber;
    private int amount;
}
