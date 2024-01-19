package com.example.sharemind.customer.domain;

import com.example.sharemind.auth.exception.AuthErrorCode;
import com.example.sharemind.auth.exception.AuthException;
import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.customer.content.Role;
import com.example.sharemind.global.common.BaseEntity;
import com.example.sharemind.global.content.Bank;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> roles;

    @Column(nullable = false)
    private String nickname;

    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "전화번호는 하이픈(-)을 포함한 10~11자리이어야 합니다.")
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "optional_agreement")
    private Boolean optionalAgreement;

    private String account;

    private Bank bank;

    @Column(name = "account_holder")
    private String accountHolder;

    @Email(message = "복구 이메일 형식이 올바르지 않습니다.")
    @Column(name = "recovery_email", nullable = false)
    private String recoveryEmail;

    @OneToOne
    @JoinColumn(name = "counselor_id", unique = true)
    private Counselor counselor;

    @Builder
    public Customer(String nickname, String email, String password, String phoneNumber, String recoveryEmail) {
        validateEmails(email, recoveryEmail);

        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.recoveryEmail = recoveryEmail;

        this.roles = new ArrayList<>() {{
            add(Role.ROLE_CUSTOMER);
        }};
    }

    public void setCounselor(Counselor counselor) {
        this.counselor = counselor;
    }

    private void validateEmails(String email, String recoveryEmail) {
        if (email.equals(recoveryEmail)) {
            throw new AuthException(AuthErrorCode.INVALID_RECOVERY_EMAIL);
        }
    }
}
