package com.xpresspayment.javabackendexercise.service;

import com.xpresspayment.javabackendexercise.model.AppUser;
import com.xpresspayment.javabackendexercise.payload.request.AirtimeVtuRequest;
import com.xpresspayment.javabackendexercise.payload.request.AirtimeVtuRequestPayload;
import org.springframework.security.core.Authentication;

public interface AirtimeVTUService {
    void buyAirtime(AirtimeVtuRequest airtimeVtuRequest, Authentication authentication);
    void saveAirtimeVtuRecord(AirtimeVtuRequestPayload airtimeVtuRequestPayload, AppUser appUser, String referenceId);
}
