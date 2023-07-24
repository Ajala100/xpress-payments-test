package com.xpresspayment.javabackendexercise.service;

import com.xpresspayment.javabackendexercise.enumerations.ERole;
import com.xpresspayment.javabackendexercise.payload.request.CreateUserRequest;
import com.xpresspayment.javabackendexercise.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceImplTest {
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    public void testCreateUser_ShouldCreateUserSuccessfully() {
        // given
        CreateUserRequest createUserRequest = new CreateUserRequest("john.doe@example.com", "password123");

        // Mocking the passwordEncoder behavior
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(createUserRequest.password())).thenReturn(encodedPassword);

        // when
        userService.createUser(createUserRequest);

        //assert and Verify that the userRepository.save() method was called with the correct user object
        verify(userRepository).save(argThat(user -> {
            return user.getEmail().equals(createUserRequest.email())
                    && user.getPassword().equals(encodedPassword)
                    && user.isEnabled()
                    && user.isLocked()
                    && user.getDateCreated().isEqual(LocalDate.now())
                    && user.getRole() == ERole.ROLE_USER;
        }));
    }

    @Test
    public void testCheckIfUserWithEmailExists_UserExists_ShouldReturnTrue() {
        // given
        String email = "john.doe@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // when
        boolean result = userService.checkIfUserWithEmailExists(email);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testCheckIfUserWithEmailExists_UserDoesNotExist_ShouldReturnFalse() {
        // given
        String email = "nonexistent@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);

        // when
        boolean result = userService.checkIfUserWithEmailExists(email);

        // Assert
        assertFalse(result);
    }
}