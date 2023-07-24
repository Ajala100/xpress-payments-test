package com.xpresspayment.javabackendexercise.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class AirtimeVtuRecord {
    @Id
    private Long id;
    @ManyToOne
    private AppUser appUser;
    private String phoneNumber;
    private int amount;
    private String uniqueCode;
    private String requestId;
    private String referenceId;
}
