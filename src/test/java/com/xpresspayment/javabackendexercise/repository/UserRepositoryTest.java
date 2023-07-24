package com.xpresspayment.javabackendexercise.repository;

import com.xpresspayment.javabackendexercise.enumerations.ERole;
import com.xpresspayment.javabackendexercise.model.AppUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void testFindUserByEmail_UserExists_ShouldReturnUser() {
        // given
        String email = "john.doe@example.com";
        AppUser user = AppUser.builder()
                .dateCreated(LocalDate.now())
                .email(email)
                .enabled(true)
                .id(1L)
                .locked(true)
                .password("password")
                .role(ERole.ROLE_USER)
                .build();
        userRepository.save(user);

        // when
        Optional<AppUser> foundUser = userRepository.findUserByEmail(email);

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(email, foundUser.get().getEmail());
    }

    @Test
    public void testFindUserByEmail_UserDoesNotExist_ShouldReturnEmptyOptional() {
        // giveb
        String email = "nonexistent@example.com";

        // when
        Optional<AppUser> foundUser = userRepository.findUserByEmail(email);

        // Assert
        assertFalse(foundUser.isPresent());
    }

    @Test
    public void testExistsByEmail_UserDoesNotExist_ShouldReturnFalse() {
        // given
        String email = "nonexistent@example.com";

        // when
        boolean exists = userRepository.existsByEmail(email);

        // Assert
        assertFalse(exists);
    }


    @Test
    public void testExistsByEmail_UserExists_ShouldReturnTrue() {
        // Arrange
        String email = "john.doe@example.com";
        AppUser user = AppUser.builder()
                .dateCreated(LocalDate.now())
                .email(email)
                .enabled(true)
                .id(1L)
                .locked(true)
                .password("password")
                .role(ERole.ROLE_USER)
                .build();
        userRepository.save(user);

        // when
        boolean exists = userRepository.existsByEmail(email);

        // Assert
        assertTrue(exists);
    }


}