package com.xpresspayment.javabackendexercise.controller;

import com.xpresspayment.javabackendexercise.payload.request.CreateUserRequest;
import com.xpresspayment.javabackendexercise.payload.response.BaseResponsePayload;
import com.xpresspayment.javabackendexercise.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RequestMapping("/api/v1/users")
@RestController
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponsePayload> registerUser(@Valid @RequestBody CreateUserRequest createUserRequest){
        userService.createUser(createUserRequest);
        return new ResponseEntity<>(new BaseResponsePayload(true, "Your account has been successfully created",
                HttpStatus.CREATED.value(), LocalDate.now()), HttpStatus.CREATED);
    }
}
