package com.example.sharemind.letterMessage.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LetterMessageUpdateRequest {

    @Schema(description = "메시지 아이디", example = "1")
    @NotNull(message = "메시지 id는 공백일 수 없습니다.")
    private Long messageId;

    @Schema(description = "메시지 내용", example = "안녕하세요 롸롸롸")
    @NotBlank(message = "메시지 내용은 공백일 수 없습니다.")
    private String content;

    @Schema(description = "임시저장/최종 제출 여부", example = "true")
    @NotNull(message = "작성 완료 여부는 공백일 수 없습니다.")
    private Boolean isCompleted;

    public static LetterMessageUpdateRequest of(LetterMessageUpdateFirstRequest letterMessageUpdateFirstRequest) {
        return new LetterMessageUpdateRequest(letterMessageUpdateFirstRequest.getMessageId(),
                letterMessageUpdateFirstRequest.getContent(), letterMessageUpdateFirstRequest.getIsCompleted());
    }
}
