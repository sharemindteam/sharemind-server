package com.example.sharemind.chatMessage.utils;

import com.example.sharemind.chat.domain.Chat;

public class ChatMessageUtil {
    public static String getFinishMessage(Chat chat, String content) {
        String nickname = chat.getConsult().getCounselor().getNickname();
        return nickname + content;
    }
}
