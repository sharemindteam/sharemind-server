package com.example.sharemind.letterMessage.dto.request;

import com.example.sharemind.letter.domain.Letter;
import com.example.sharemind.letterMessage.domain.LetterMessage;
import com.example.sharemind.letterMessage.content.LetterMessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LetterMessageCreateRequest {

    @NotNull(message = "편지 id는 공백일 수 없습니다.")
    private Long letterId;

    @NotBlank(message = "메시지 유형은 공백일 수 없습니다.")
    private String messageType;

    @NotBlank(message = "메시지 내용은 공백일 수 없습니다.")
    private String content;

    @NotNull(message = "작성 완료 여부는 공백일 수 없습니다.")
    private Boolean isCompleted;

    public static LetterMessageCreateRequest of(LetterMessageCreateFirstRequest letterMessageCreateFirstRequest) {
        return new LetterMessageCreateRequest(letterMessageCreateFirstRequest.getLetterId(), LetterMessageType.FIRST_QUESTION.getDisplayName(),
                letterMessageCreateFirstRequest.getContent(), letterMessageCreateFirstRequest.getIsCompleted());
    }

    public LetterMessage toEntity(Letter letter, LetterMessageType messageType) {
        return LetterMessage.builder()
                .letter(letter)
                .messageType(messageType)
                .content(content)
                .isCompleted(isCompleted)
                .build();
    }
}
