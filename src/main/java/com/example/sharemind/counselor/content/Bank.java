package com.example.sharemind.counselor.content;

import com.example.sharemind.counselor.exception.CounselorErrorCode;
import com.example.sharemind.counselor.exception.CounselorException;
import java.util.Arrays;

public enum Bank {
    WOORI("우리은행"),
    KB_KOOKMIN("KB국민은행"),
    NH_NONGHYUP("NH농협은행"),
    IBK_KIUP("IBK기업은행"),
    CITI("씨티은행"),
    SAEMAUL("새마을금고"),
    JEONBUK("전북은행"),
    POSTBANK("우체국"),
    KYONGNAM("경남은행"),
    SUHYUP("수협"),
    SANGHO("상호저축은행"),
    TOSS("토스뱅크"),
    KAKAO("카카오뱅크"),
    SHINHAN("신한은행"),
    HANA("KEB하나은행"),
    SC("SC제일은행"),
    KDB_SANUP("KDB산업은행"),
    DAEGU("대구은행"),
    KWANGJU("광주은행"),
    SHINHYUP("신협"),
    BNK_BUSAN("부산은행"),
    JEJU("제주은행"),
    K("케이뱅크");

    private final String displayName;

    Bank(String displayName) {
        this.displayName = displayName;
    }

    public static void existsByDisplayName(String displayName) {
        Arrays.stream(Bank.values())
                .filter(bank -> bank.displayName.equalsIgnoreCase(displayName))
                .findAny().orElseThrow(() -> new CounselorException(CounselorErrorCode.BANK_NOT_FOUND, displayName));
    }
}
