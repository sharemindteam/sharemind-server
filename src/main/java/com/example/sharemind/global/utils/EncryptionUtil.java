package com.example.sharemind.global.utils;

import com.example.sharemind.global.exception.GlobalErrorCode;
import com.example.sharemind.global.exception.GlobalException;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EncryptionUtil {

    private static StringEncryptor stringEncryptor;

    @Autowired
    public void setStringEncryptor(StringEncryptor encryptor) {
        EncryptionUtil.stringEncryptor = encryptor;
    }

    public static String encrypt(Long id) {
        return stringEncryptor.encrypt(id.toString());
    }

    public static Long decrypt(String id) {
        try {
            String decryptedData = stringEncryptor.decrypt(id);
            return Long.parseLong(decryptedData);
        } catch (Exception e) {
            throw new GlobalException(GlobalErrorCode.INVALID_ID, id);
        }
    }
}
