package com.xpresspayment.javabackendexercise.service;

import com.google.gson.Gson;
import com.xpresspayment.javabackendexercise.exception.customException.ServersideErrorException;
import com.xpresspayment.javabackendexercise.model.AirtimeVtuRecord;
import com.xpresspayment.javabackendexercise.model.AppUser;
import com.xpresspayment.javabackendexercise.payload.request.AirtimeVtuRequest;
import com.xpresspayment.javabackendexercise.payload.request.AirtimeVtuRequestDetails;
import com.xpresspayment.javabackendexercise.payload.request.AirtimeVtuRequestPayload;
import com.xpresspayment.javabackendexercise.payload.response.AirtimeVtuResponsePayload;
import com.xpresspayment.javabackendexercise.repository.AirtimeVtuRepository;
import com.xpresspayment.javabackendexercise.utility.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AirtimeVtuServiceServiceImpl implements AirtimeVTUService {
    private final HttpClient httpClient;
    private final Gson gson;
    private final AirtimeVtuRepository airtimeVtuRepository;

    @Value("${xpress_payments_api_key}")
    private String apiKey;
    @Value("${xpress_payments_api_secret}")
    private String apiSecret;
    @Value("${xpress_payments_airtime_topup_url}")
    private String airtimeVtuUrl;

    @Override
    public void buyAirtime(AirtimeVtuRequest airtimeVtuRequest, Authentication authentication) {
        AppUser appUser = (AppUser)authentication.getPrincipal();
        AirtimeVtuRequestPayload airtimeVtuRequestPayload = createTimeVtuRequest(airtimeVtuRequest);
        String requestBody = gson.toJson(airtimeVtuRequestPayload);

        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(airtimeVtuUrl))
                .setHeader("Authorization", "Bearer ".concat(apiKey))
                .setHeader("PaymentHash", getHmacValue())
                .setHeader("Channel", "API")
                .setHeader("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();

        try{
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            AirtimeVtuResponsePayload airtimeVtuResponsePayload = gson.fromJson(response.body(), AirtimeVtuResponsePayload.class);
            if(!airtimeVtuResponsePayload.getResponseCode().equals("00")) throw new ServersideErrorException("Request could not be completed");
            saveAirtimeVtuRecord(airtimeVtuRequestPayload, appUser, airtimeVtuResponsePayload.getReferenceId());

        } catch (IOException | InterruptedException e) {
           log.error("An error occurred while trying to perform airtime VTU. caused by {}", e.getMessage());
           throw new ServersideErrorException("Request could not be completed");
        }
    }

    @Override
    public void saveAirtimeVtuRecord(AirtimeVtuRequestPayload airtimeVtuRequestPayload, AppUser appUser, String referenceId) {
        AirtimeVtuRecord airtimeVtuRecord = createAirtimeVtuRecord(airtimeVtuRequestPayload, appUser, referenceId);
        airtimeVtuRepository.save(airtimeVtuRecord);
    }

    private AirtimeVtuRecord createAirtimeVtuRecord(AirtimeVtuRequestPayload airtimeVtuRequestPayload, AppUser appUser, String referenceId){
        return AirtimeVtuRecord.builder()
                .appUser(appUser)
                .amount(airtimeVtuRequestPayload.getDetails().getAmount())
                .id(IdGenerator.generateId())
                .referenceId(referenceId)
                .phoneNumber(airtimeVtuRequestPayload.getDetails().getPhoneNumber())
                .uniqueCode(airtimeVtuRequestPayload.getUniqueCode())
                .requestId(airtimeVtuRequestPayload.getRequestId())
                .build();
    }

    private AirtimeVtuRequestPayload createTimeVtuRequest(AirtimeVtuRequest airtimeVtuRequest){
        return new AirtimeVtuRequestPayload(String.valueOf(IdGenerator.generateId()),
                airtimeVtuRequest.uniqueCode(), new AirtimeVtuRequestDetails(airtimeVtuRequest.phoneNumber(), airtimeVtuRequest.amount()));
    }

    private String getHmacValue(){
        String toHash = """
                {
                    "size": 1,
                    "page": 1
                }""";
        return calculateHMAC512(toHash, apiSecret);
    }

    public String calculateHMAC512(String data, String key) {

        String HMAC_SHA512 = "HmacSHA512";
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), HMAC_SHA512);
        Mac mac = null;
        try {
            mac = Mac.getInstance(HMAC_SHA512);

            mac.init(secretKeySpec);
            return String.valueOf(Hex.encode(mac.doFinal(data.getBytes())));

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("Unable to calculate HMAC5312. caused by {}", e.getMessage());
            throw new ServersideErrorException("Your request to top up your airtime could not be completed");

        }
    }
}
