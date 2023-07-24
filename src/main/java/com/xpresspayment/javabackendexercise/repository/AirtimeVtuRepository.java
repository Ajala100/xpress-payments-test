package com.xpresspayment.javabackendexercise.repository;

import com.xpresspayment.javabackendexercise.model.AirtimeVtuRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirtimeVtuRepository extends JpaRepository<AirtimeVtuRecord, Long> {
}
