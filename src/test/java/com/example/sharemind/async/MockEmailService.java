package com.example.sharemind.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MockEmailService {

    @Async
    public void sendEmail() {
        try {
            Thread.sleep(1000);
            log.info("Send email: " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
