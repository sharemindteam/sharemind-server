package com.example.sharemind.chat.content;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public enum ChatWebsocketStatus {

    @Schema(description = "채팅 생성")
    CHAT_CREATE,

    @Schema(description = "채팅 시작")
    CHAT_START,

    @Schema(description = "채팅 요청 취소(요청 시간이 10분 지난 경우)")
    CHAT_START_REQUEST_CANCEL,

    @Schema(description = "채팅 종료 5분 전")
    CHAT_LEFT_FIVE_MINUTE,

    @Schema(description = "채팅 시간 종료")
    CHAT_TIME_OVER,

    @Schema(description = "채팅 종료")
    CHAT_FINISH,

    @Schema(description = "상담사가 채팅 시작 요청")
    COUNSELOR_CHAT_START_REQUEST,

    @Schema(description = "구매자가 채팅 시작 수락")
    CUSTOMER_CHAT_START_RESPONSE,

    @Schema(description = "구매자가 채팅 종료")
    CUSTOMER_CHAT_FINISH_REQUEST,

    @Schema(description = "구매자의 채팅 리뷰 창")
    CUSTOMER_CHAT_REVIEW,
}
