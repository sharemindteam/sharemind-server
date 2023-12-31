package com.example.sharemind.auth.application;

import com.example.sharemind.auth.dto.request.AuthSignInRequest;
import com.example.sharemind.auth.dto.request.AuthSignUpRequest;
import com.example.sharemind.auth.dto.response.TokenDto;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.customer.exception.CustomerErrorCode;
import com.example.sharemind.customer.exception.CustomerException;
import com.example.sharemind.customer.repository.CustomerRepository;
import com.example.sharemind.global.common.BaseEntity;
import com.example.sharemind.global.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
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

        if (customerRepository.existsByEmail(authSignUpRequest.getEmail())) {
            throw new CustomerException(CustomerErrorCode.EMAIL_ALREADY_EXIST, authSignUpRequest.getEmail());
        }

        Customer customer = authSignUpRequest.toEntity(passwordEncoder.encode(authSignUpRequest.getPassword()));
        customerRepository.save(customer);
    }

    @Transactional
    @Override
    public TokenDto signIn(AuthSignInRequest authSignInRequest) {

        Customer customer = customerRepository.findByEmail(authSignInRequest.getEmail())
                .filter(BaseEntity::isActivated)
                .orElseThrow(() -> new CustomerException(CustomerErrorCode.CUSTOMER_NOT_FOUND, authSignInRequest.getEmail()));

        if (!passwordEncoder.matches(authSignInRequest.getPassword(), customer.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = tokenProvider.createAccessToken(customer.getEmail(), customer.getRoles());
        String refreshToken = tokenProvider.createRefreshToken(customer.getEmail());

        return TokenDto.of(accessToken, refreshToken);
    }
}
