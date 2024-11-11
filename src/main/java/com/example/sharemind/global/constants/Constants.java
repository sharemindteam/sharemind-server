package com.example.sharemind.global.constants;

public class Constants {
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String CUSTOMER_PREFIX = "customer: ";
    public static final String COUNSELOR_PREFIX = "counselor: ";

    public static final String CUSTOMER_CHATTING_PREFIX = "customer chatting: "; // 현재 접속중인 방
    public static final String COUNSELOR_CHATTING_PREFIX = "counselor chatting: ";

    public static final String REALTIME_COUNSELOR = "realtimeCounselors";

    public static final Integer CUSTOMER_ONGOING_CONSULT = 1;
    public static final Integer COUNSELOR_ONGOING_CONSULT = 3;

    public static final Boolean IS_CUSTOMER = true;
    public static final Boolean IS_COUNSELOR = false;

    public static final Boolean IS_CHAT = true;
    public static final Boolean IS_LETTER = false;

    public static final Double FEE = 0.2;

    public static final Long MAX_COMMENTS = 5L;
}
