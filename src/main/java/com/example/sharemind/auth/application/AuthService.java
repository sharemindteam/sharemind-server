package com.example.sharemind.auth.application;

import com.example.sharemind.auth.dto.request.AuthSignUpRequest;

public interface AuthService {
    void signUp(AuthSignUpRequest authSignUpRequest);
}
