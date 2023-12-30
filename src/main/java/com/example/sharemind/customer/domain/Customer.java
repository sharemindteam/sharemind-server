package com.example.sharemind.customer.domain;

import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.global.common.BaseEntity;
import com.example.sharemind.global.content.Bank;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    private String name;

    private String nickname;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String email;

    private String password;

    @Column(name = "optional_agreement")
    private Boolean optionalAgreement;

    private String account;

    private Bank bank;

    @Column(name = "account_holder")
    private String accountHolder;

    @Column(name = "recovery_email")
    private String recoveryEmail;

    @OneToOne
    @JoinColumn(name = "counselor_id", unique = true)
    private Counselor counselor;
}
