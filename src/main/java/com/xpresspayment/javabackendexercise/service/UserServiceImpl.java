package com.xpresspayment.javabackendexercise.service;

import com.xpresspayment.javabackendexercise.enumerations.ERole;
import com.xpresspayment.javabackendexercise.model.AppUser;
import com.xpresspayment.javabackendexercise.payload.request.CreateUserRequest;
import com.xpresspayment.javabackendexercise.repository.UserRepository;
import com.xpresspayment.javabackendexercise.utility.IdGenerator;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public void createUser(CreateUserRequest createUserRequest) {
        AppUser user = buildUser(createUserRequest);
        saveUser(user);
    }

    @Override
    public boolean checkIfUserWithEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    private AppUser buildUser(CreateUserRequest createUserRequest){
        return AppUser.builder()
                .email(createUserRequest.email())
                .id(IdGenerator.generateId())
                .password(passwordEncoder.encode(createUserRequest.password()))
                .enabled(true)
                .locked(true)
                .dateCreated(LocalDate.now())
                .role(ERole.ROLE_USER)
                .build();
    }

    private void saveUser(AppUser user){
        userRepository.save(user);
    }
}
