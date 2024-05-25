package com.example.sharemind.async;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AsyncTest {

    @Autowired
    MockEmailService mockEmailService;

    @Test
    @DisplayName("sendEmail 메서드가 10개의 스레드를 활용하여 비동기로 처리됨을 확인할 수 있다.")
    void testSendEmail() throws InterruptedException {
        for (int i = 0; i < 50; i++) {
            mockEmailService.sendEmail();
        }
        Thread.sleep(10000);
    }
}
