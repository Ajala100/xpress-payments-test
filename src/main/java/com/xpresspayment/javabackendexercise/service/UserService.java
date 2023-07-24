package com.xpresspayment.javabackendexercise.service;

import com.xpresspayment.javabackendexercise.payload.request.CreateUserRequest;

public interface UserService {
    void createUser(CreateUserRequest createUserRequest);
    boolean checkIfUserWithEmailExists(String email);
}
