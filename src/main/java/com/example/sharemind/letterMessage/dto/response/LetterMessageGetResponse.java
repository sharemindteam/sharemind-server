package com.example.sharemind.letterMessage.dto.response;

import com.example.sharemind.global.utils.TimeUtil;
import com.example.sharemind.letterMessage.domain.LetterMessage;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LetterMessageGetResponse {

    @Schema(description = "메시지 아이디, 메시지 존재하지 않으면 null", example = "1")
    private final Long messageId;

    @Schema(description = "메시지 유형, 메시지 존재하지 않으면 null", example = "추가 답장")
    private final String messageType;

    @Schema(description = "메시지 내용, 메시지 존재하지 않으면 null", example = "니냐니뇨")
    private final String content;

    @Schema(description = "작성 일시, 메시지 존재하지 않으면 null", example = "2024년 1월 4일 오전 9시 45분", type = "string")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 MM월 dd일 a HH시 mm분")
    private final LocalDateTime updatedAt;

    @Schema(description = "작성 일시, 메시지 존재하지 않으면 null", example = "01.12")
    private final String updatedAt2;

    public static LetterMessageGetResponse of(LetterMessage letterMessage) {
        return new LetterMessageGetResponse(letterMessage.getMessageId(), letterMessage.getMessageType().getDisplayName(),
                letterMessage.getContent(), letterMessage.getUpdatedAt(),
                TimeUtil.getUpdatedAt(letterMessage.getUpdatedAt()));
    }

    public static LetterMessageGetResponse of() {
        return new LetterMessageGetResponse(null, null, null, null, null);
    }
}
