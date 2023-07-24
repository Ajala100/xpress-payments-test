package com.xpresspayment.javabackendexercise.controller;

import com.xpresspayment.javabackendexercise.payload.request.AirtimeVtuRequest;
import com.xpresspayment.javabackendexercise.payload.response.BaseResponsePayload;
import com.xpresspayment.javabackendexercise.service.AirtimeVTUService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/airtime")
@AllArgsConstructor
public class VtuController {
    private AirtimeVTUService airtimeVTUService;

    @PostMapping("/top-up")
    public ResponseEntity<BaseResponsePayload> virtualTopUp(@RequestBody @Valid AirtimeVtuRequest airtimeVtuRequest, Authentication authentication){
        airtimeVTUService.buyAirtime(airtimeVtuRequest, authentication);
        return new ResponseEntity<>(new BaseResponsePayload(true, "Successful",
                HttpStatus.OK.value(), LocalDate.now()), HttpStatus.OK);
    }
}
