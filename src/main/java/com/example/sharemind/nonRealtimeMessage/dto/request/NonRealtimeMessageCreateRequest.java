package com.example.sharemind.nonRealtimeMessage.dto.request;

import com.example.sharemind.nonRealtimeConsult.domain.NonRealtimeConsult;
import com.example.sharemind.nonRealtimeMessage.domain.NonRealtimeMessage;
import com.example.sharemind.nonRealtimeMessage.content.NonRealtimeMessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class NonRealtimeMessageCreateRequest {

    @NotNull(message = "비실시간 상담 id는 공백일 수 없습니다.")
    private Long nonRealtimeConsultId;

    @NotBlank(message = "메시지 유형은 공백일 수 없습니다.")
    private String messageType;

    @NotBlank(message = "메시지 내용은 공백일 수 없습니다.")
    private String content;

    @NotNull(message = "작성 완료 여부는 공백일 수 없습니다.")
    private Boolean isCompleted;

    public NonRealtimeMessage toEntity(NonRealtimeConsult nonRealtimeConsult, NonRealtimeMessageType messageType) {
        return NonRealtimeMessage.builder()
                .nonRealtimeConsult(nonRealtimeConsult)
                .messageType(messageType)
                .content(content)
                .isCompleted(isCompleted)
                .build();
    }
}
