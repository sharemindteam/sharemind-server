package com.example.sharemind.auth.application;

import com.example.sharemind.auth.dto.request.AuthReissueRequest;
import com.example.sharemind.auth.dto.request.AuthSignInRequest;
import com.example.sharemind.auth.dto.request.AuthSignUpRequest;
import com.example.sharemind.auth.dto.response.TokenDto;
import com.example.sharemind.customer.domain.Customer;

public interface AuthService {
    void signUp(AuthSignUpRequest authSignUpRequest);
    TokenDto signIn(AuthSignInRequest authSignInRequest);
    TokenDto reissueToken(AuthReissueRequest authReissueRequest, Customer customer);
}
