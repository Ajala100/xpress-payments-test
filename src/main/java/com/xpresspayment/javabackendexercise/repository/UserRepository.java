package com.xpresspayment.javabackendexercise.repository;

import com.xpresspayment.javabackendexercise.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findUserByEmail(String email);
    boolean existsByEmail(String email);
}
