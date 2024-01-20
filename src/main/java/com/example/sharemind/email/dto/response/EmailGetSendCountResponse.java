package com.example.sharemind.email.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailGetSendCountResponse {
    @Schema(description = "이메일 보낸 횟수", example = "1")
    private final int count;

    public static EmailGetSendCountResponse of(int count) {
        return new EmailGetSendCountResponse(count);
    }
}
