package com.example.sharemind.consult.dto.response;

import com.example.sharemind.global.dto.response.ChatLetterGetResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ConsultGetOngoingResponse {

    @Schema(description = "진행 중인 상담 수")
    private final Integer totalOngoing;

    @Schema(description = "진행 중인 최근 상담")
    private final List<ChatLetterGetResponse> responses;

    public static ConsultGetOngoingResponse of(Integer totalOngoing, List<ChatLetterGetResponse> responses) {
        return new ConsultGetOngoingResponse(totalOngoing, responses);
    }
}
