package com.example.sharemind.customer;

import com.example.sharemind.customer.domain.Customer;
import java.util.ArrayList;
import java.util.List;

public class CustomerDataFactory {

    public static List<Customer> createCustomers(String emailPrefix, int size) {
        List<Customer> customers = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Customer customer = Customer.builder()
                    .email(emailPrefix + i + "@gmail.com")
                    .password("abcde")
                    .build();

            customers.add(customer);
        }

        return customers;
    }
}
