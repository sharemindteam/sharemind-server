package com.example.sharemind.customer.domain;

import com.example.sharemind.counselor.domain.Counselor;
import com.example.sharemind.customer.content.Role;
import com.example.sharemind.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @Column(nullable = false, updatable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean isBanned;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counselor_id", unique = true)
    private Counselor counselor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quit_id", unique = true)
    private Quit quit;

    @Builder
    public Customer(String email, String password) {
        this.nickname = "셰어" + new Random().nextInt(999999);
        this.email = email;
        this.password = password;
        this.isBanned = false;

        this.roles = new ArrayList<>() {{
            add(Role.ROLE_CUSTOMER);
        }};
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateIsBanned(Boolean isBanned) {
        this.isBanned = isBanned;
    }

    public void setCounselor(Counselor counselor) {
        this.counselor = counselor;
    }

    public void setQuit(Quit quit) {
        this.quit = quit;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }
}
