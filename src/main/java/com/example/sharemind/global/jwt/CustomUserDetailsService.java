package com.example.sharemind.global.jwt;

import com.example.sharemind.customer.domain.Customer;
import com.example.sharemind.customer.exception.CustomerErrorCode;
import com.example.sharemind.customer.exception.CustomerException;
import com.example.sharemind.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Customer customer = customerRepository.findByEmailAndIsActivatedIsTrue(username)
                .orElseThrow(() -> new CustomerException(CustomerErrorCode.CUSTOMER_NOT_FOUND, username));

        return new CustomUserDetails(customer);
    }
}
