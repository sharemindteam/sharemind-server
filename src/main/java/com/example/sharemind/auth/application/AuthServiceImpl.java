package com.example.sharemind.auth.application;

import com.example.sharemind.auth.dto.request.AuthSignUpRequest;
import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.customer.exception.CustomerErrorCode;
import com.example.sharemind.customer.exception.CustomerException;
import com.example.sharemind.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

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
}
