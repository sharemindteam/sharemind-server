package com.example.sharemind.auth.application;

import com.example.sharemind.auth.dto.request.*;
import com.example.sharemind.auth.dto.response.TokenDto;

public interface AuthService {
    void signUp(AuthSignUpRequest authSignUpRequest);

    TokenDto signIn(AuthSignInRequest authSignInRequest);

    TokenDto reissueToken(AuthReissueRequest authReissueRequest);

    void checkDuplicateEmail(String email);

    Boolean getPasswordMatched(AuthGetPasswordMatchRequest authGetPasswordMatchRequest, Long customerId);

    void updatePassword(AuthUpdatePasswordRequest authUpdatePasswordRequest, Long customerId);

    void quit(AuthQuitRequest authQuitRequest, Long customerId);

    void signOut(AuthSignOutRequest authSignOutRequest);

    void updateAndSendPasswordByEmail(AuthFindPasswordRequest authFindPasswordRequest);
}
