package com.example.sharemind.nonRealtimeMessage.dto.request;

import com.example.sharemind.letter.domain.Letter;
import com.example.sharemind.nonRealtimeMessage.domain.LetterMessage;
import com.example.sharemind.nonRealtimeMessage.content.LetterMessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LetterMessageCreateRequest {

    @NotNull(message = "비실시간 상담 id는 공백일 수 없습니다.")
    private Long letterId;

    @NotBlank(message = "메시지 유형은 공백일 수 없습니다.")
    private String messageType;

    @NotBlank(message = "메시지 내용은 공백일 수 없습니다.")
    private String content;

    @NotNull(message = "작성 완료 여부는 공백일 수 없습니다.")
    private Boolean isCompleted;

    public LetterMessage toEntity(Letter letter, LetterMessageType messageType) {
        return LetterMessage.builder()
                .letter(letter)
                .messageType(messageType)
                .content(content)
                .isCompleted(isCompleted)
                .build();
    }
}
