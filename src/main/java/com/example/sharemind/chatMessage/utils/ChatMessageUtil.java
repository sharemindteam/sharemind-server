package com.example.sharemind.chatMessage.utils;

import com.example.sharemind.chat.domain.Chat;

public class ChatMessageUtil {
    private static final String COUNSELOR_CHAT_FINISH = "님과 상담이 종료되었어요.";
    public static String getFinishMessage(Chat chat, String content, Boolean isCustomer) {
        if (!isCustomer) {
            return chat.getConsult().getCustomer().getNickname() + COUNSELOR_CHAT_FINISH;
        }
        String nickname = chat.getConsult().getCounselor().getNickname();
        return nickname + content;
    }
}
