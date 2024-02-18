package com.example.sharemind.chatMessage.dto.request;

import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.chatMessage.content.ChatMessageStatus;
import com.example.sharemind.chatMessage.domain.ChatMessage;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ChatMessageCreateRequest {

    @NotNull(message = "채팅 내용은 공백일 수 없습니다.")
    private String content;

    public ChatMessage toEntity(Chat chat, Boolean isCustomer, ChatMessageStatus messageStatus) {
        return ChatMessage.builder()
                .chat(chat)
                .isCustomer(isCustomer)
                .messageStatus(messageStatus)
                .content(content)
                .build();
    }
}
