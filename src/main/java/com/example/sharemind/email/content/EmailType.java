package com.example.sharemind.email.content;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmailType {

    SIGNUP_CODE("sharemind 회원 가입 인증 코드입니다.",
            """
                    sharemind 회원 가입 인증 코드입니다.
                    5분 내에 입력해주세요.
                    코드 : 
                    """),
    FIND_ID("sharemind 아이디입니다.",
            """
                    sharemind 아이디 정보 입니다.
                    """),
    FIND_PASSWORD("sharemind 임시 비밀번호입니다.",
            """
                    sharemind 임시 비밀번호입니다.
                    사이트에 접속하여 새로운 비밀번호로 변경해주세요.
                    """);

    private final String subject;
    private final String text;
}
