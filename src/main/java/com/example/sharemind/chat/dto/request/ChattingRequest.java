//todo: 삭제
package com.example.sharemind.chat.dto.request;

import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChattingRequest {
    private String content;
}
