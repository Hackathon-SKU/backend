package com.hackathon.backend.domain.Posting.support;
// 공용으로 둘 거면 package를 com.hackathon.backend.common.util 로 바꾸세요

import java.util.Map;

public final class SimpleDayTimeMapper {
    private SimpleDayTimeMapper() {}

    // 요일: 1..7
    private static final Map<String, Integer> DAY_KO_TO_CODE = Map.of(
            "월",1, "화",2, "수",3, "목",4, "금",5, "토",6, "일",7
    );
    private static final String[] DAY_CODE_TO_KO = {
            "", "월","화","수","목","금","토","일"
    };

    public static int dayKoToCode(String ko) {
        Integer v = DAY_KO_TO_CODE.get(ko);
        if (v == null) throw new IllegalArgumentException("지원하지 않는 요일: " + ko);
        return v;
    }
    public static String dayCodeToKo(int code) {
        if (code < 1 || code > 7) throw new IllegalArgumentException("잘못된 요일 코드: " + code);
        return DAY_CODE_TO_KO[code];
    }

    // 시간대: 1=오전, 2=오후, 3=심야
    private static final Map<String, Integer> BAND_KO_TO_CODE = Map.of(
            "오전",1, "오후",2, "심야",3
    );
    private static final String[] BAND_CODE_TO_KO = {
            "", "오전","오후","심야"
    };

    public static int bandKoToCode(String ko) {
        Integer v = BAND_KO_TO_CODE.get(ko);
        if (v == null) throw new IllegalArgumentException("지원하지 않는 시간대: " + ko);
        return v;
    }
    public static String bandCodeToKo(int code) {
        if (code < 1 || code > 3) throw new IllegalArgumentException("잘못된 시간대 코드: " + code);
        return BAND_CODE_TO_KO[code];
    }
}
