package com.example.sharemind.auth.application;

import com.example.sharemind.auth.dto.request.*;
import com.example.sharemind.auth.dto.response.TokenDto;
import com.example.sharemind.auth.exception.AuthErrorCode;
import com.example.sharemind.auth.exception.AuthException;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.customer.exception.CustomerErrorCode;
import com.example.sharemind.customer.exception.CustomerException;
import com.example.sharemind.customer.repository.CustomerRepository;
import com.example.sharemind.global.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;

    @Transactional
    @Override
    public void signUp(AuthSignUpRequest authSignUpRequest) {
        if (customerRepository.existsByEmailAndIsActivatedIsTrue(authSignUpRequest.getEmail())) {
            throw new AuthException(AuthErrorCode.EMAIL_ALREADY_EXIST, authSignUpRequest.getEmail());
        }

        Customer customer = authSignUpRequest.toEntity(passwordEncoder.encode(authSignUpRequest.getPassword()));
        customerRepository.save(customer);
    }

    @Override
    public TokenDto signIn(AuthSignInRequest authSignInRequest) {
        Customer customer = customerRepository.findByEmailAndIsActivatedIsTrue(authSignInRequest.getEmail())
                .orElseThrow(() -> new CustomerException(CustomerErrorCode.CUSTOMER_NOT_FOUND, authSignInRequest.getEmail()));

        if (!passwordEncoder.matches(authSignInRequest.getPassword(), customer.getPassword())) {
            throw new AuthException(AuthErrorCode.INVALID_PASSWORD);
        }

        String accessToken = tokenProvider.createAccessToken(customer.getEmail(), customer.getRoles());
        String refreshToken = tokenProvider.createRefreshToken(customer.getEmail());

        return TokenDto.of(accessToken, refreshToken);
    }

    @Override
    public TokenDto reissueToken(AuthReissueRequest authReissueRequest) {
        if(!tokenProvider.validateRefreshToken(authReissueRequest.getRefreshToken())) {
            throw new AuthException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }

        String email = tokenProvider.getEmail(authReissueRequest.getRefreshToken());
        Customer customer = customerRepository.findByEmailAndIsActivatedIsTrue(email)
                .orElseThrow(() -> new CustomerException(CustomerErrorCode.CUSTOMER_NOT_FOUND, email));

        String accessToken = tokenProvider.createAccessToken(customer.getEmail(), customer.getRoles());
        String refreshToken = tokenProvider.createRefreshToken(customer.getEmail());

        return TokenDto.of(accessToken, refreshToken);
    }

    @Override
    public void checkDuplicateEmail(String email) {
        if (customerRepository.existsByEmailAndIsActivatedIsTrue(email)) {
            throw new AuthException(AuthErrorCode.EMAIL_ALREADY_EXIST, email);
        }
    }

    @Override
    public Boolean getPasswordMatched(AuthGetPasswordMatchRequest authGetPasswordMatchRequest, Long customerId) {
        Customer customer = customerRepository.findByCustomerIdAndIsActivatedIsTrue(customerId)
                .orElseThrow(() -> new CustomerException(CustomerErrorCode.CUSTOMER_NOT_FOUND, customerId.toString()));

        return passwordEncoder.matches(authGetPasswordMatchRequest.getPassword(), customer.getPassword());
    }

    @Transactional
    @Override
    public void updatePassword(AuthUpdatePasswordRequest authUpdatePasswordRequest, Long customerId) {
        Customer customer = customerRepository.findByCustomerIdAndIsActivatedIsTrue(customerId)
                .orElseThrow(() -> new CustomerException(CustomerErrorCode.CUSTOMER_NOT_FOUND, customerId.toString()));
        if (passwordEncoder.matches(authUpdatePasswordRequest.getPassword(), customer.getPassword())) {
            throw new AuthException(AuthErrorCode.DUPLICATE_PASSWORD);
        }

        customer.updatePassword(passwordEncoder.encode(authUpdatePasswordRequest.getPassword()));
    }
}
