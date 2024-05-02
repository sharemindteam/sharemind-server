package com.example.sharemind.chatMessage.utils;

import com.example.sharemind.chat.domain.Chat;

public class ChatMessageUtil {
    private static final String COUNSELOR_CHAT_FINISH = "님과 상담이 종료되었어요.";
    private static final String COUNSELOR_CHAT_SEND_REQUEST = "님의 상담 수락을 기다리고 있어요.";

    public static String getFinishMessage(Chat chat, String content, Boolean isCustomer) {
        if (!isCustomer) {
            return chat.getConsult().getCustomer().getNickname() + COUNSELOR_CHAT_FINISH;
        }
        String nickname = chat.getConsult().getCounselor().getNickname();
        return nickname + content;
    }

    public static String getCounselorSendRequestMessage(Chat chat) {
        return chat.getConsult().getCustomer().getNickname() + COUNSELOR_CHAT_SEND_REQUEST;
    }
}
