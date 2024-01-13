package com.example.sharemind.chat.dto.response;

import com.example.sharemind.chat.domain.Chat;
import com.example.sharemind.chatMessage.domain.ChatMessage;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatInfoGetResponse {

    private final Long chatId;
    private final String opponentNickname;
    private final String chatStatus;

    private final int unreadMessageCount;
    private final String latestMessageContent;
    private final LocalDateTime latestMessageUpdatedAt;
    private final Boolean latestMessageIsCustomer;

    public static ChatInfoGetResponse of(String nickname, int unreadMessageCount, Chat chat, ChatMessage chatMessage) {
        if (chatMessage == null) {
            return new ChatInfoGetResponse(chat.getChatId(), nickname, chat.getChatStatus()
                    .getDisplayName(), unreadMessageCount, null, null, null);
        }
        return new ChatInfoGetResponse(chat.getChatId(), nickname, chat.getChatStatus()
                .getDisplayName(), unreadMessageCount, chatMessage.getContent(), chatMessage.getUpdatedAt(),
                chatMessage.getIsCustomer());
    }
}
