package com.example.sharemind.letterMessage.dto.request;

import com.example.sharemind.letter.domain.Letter;
import com.example.sharemind.letterMessage.domain.LetterMessage;
import com.example.sharemind.letterMessage.content.LetterMessageType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LetterMessageCreateRequest {

    @Schema(description = "편지 아이디", example = "1")
    @NotNull(message = "편지 id는 공백일 수 없습니다.")
    private Long letterId;

    @Schema(description = "메시지 유형", example = "first_Reply")
    @NotBlank(message = "메시지 유형은 공백일 수 없습니다.")
    private String messageType;

    @Schema(description = "메시지 내용", example = "안녕하세요 어쩌구저쩌구~")
    @NotBlank(message = "메시지 내용은 공백일 수 없습니다.")
    private String content;

    @Schema(description = "임시저장/최종 제출 여부", example = "true")
    @NotNull(message = "작성 완료 여부는 공백일 수 없습니다.")
    private Boolean isCompleted;

    public static LetterMessageCreateRequest of(LetterMessageCreateFirstRequest letterMessageCreateFirstRequest) {
        return new LetterMessageCreateRequest(letterMessageCreateFirstRequest.getLetterId(), LetterMessageType.FIRST_QUESTION.name(),
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
