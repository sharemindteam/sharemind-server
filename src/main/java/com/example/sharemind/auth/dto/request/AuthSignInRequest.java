package com.example.sharemind.auth.dto.request;

import lombok.Getter;

@Getter
public class AuthSignInRequest {

    private String email;
    private String password;
}
