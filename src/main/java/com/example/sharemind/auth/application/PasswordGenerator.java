package com.example.sharemind.auth.application;

import org.apache.commons.lang3.RandomStringUtils;

public class PasswordGenerator {

    public static String generateTemporaryPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%";
        return RandomStringUtils.random(12, characters);
    }
}
