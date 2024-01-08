package com.example.sharemind.realtimeConsult.dto.request;

import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChattingRequest {
    private String content;
}
