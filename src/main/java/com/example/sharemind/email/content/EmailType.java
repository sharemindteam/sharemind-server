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
    FIND_PASSWORD("sharemind 임시 비밀번호입니다.",
            """
                    sharemind 임시 비밀번호입니다.
                    사이트에 접속하여 새로운 비밀번호로 변경해주세요.
                    """),
    COUNSELOR_PROFILE_COMPLETE("sharemind 마인더 판매정보 검토 결과 안내드립니다.",
            """
                    안녕하세요, sharemind 입니다.
                    마인더 판매정보 검토 결과, 판매정보 수정이 승인되었습니다.
                    지금부터 수정된 판매정보로 마인더 활동이 가능합니다.
                    """),
    COUNSELOR_PROFILE_FAIL("sharemind 마인더 판매정보 검토 결과 안내드립니다.",
            """
                    안녕하세요, sharemind 입니다.
                    마인더 판매정보 검토 결과, 판매정보 수정이 반려되었습니다.
                    판매정보를 다시 수정해주세요.
                    """);

    private final String subject;
    private final String text;
}
