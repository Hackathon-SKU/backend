// src/main/java/com/hackathon/backend/domain/Posting/Model/DayCode.java
package com.hackathon.backend.domain.Posting.Model;

import java.util.Map;

public enum DayCode {
    MON(1, "월"),
    TUE(2, "화"),
    WED(3, "수"),
    THU(4, "목"),
    FRI(5, "금"),
    SAT(6, "토"),
    SUN(7, "일");

    private final int code;     // DB 저장용 숫자 (1~7)
    private final String ko;    // 한국어 표기

    DayCode(int code, String ko) {
        this.code = code;
        this.ko = ko;
    }
    public int code() { return code; }
    public String ko() { return ko; }

    private static final Map<String, DayCode> KO_MAP = Map.of(
            "월", MON, "화", TUE, "수", WED, "목", THU, "금", FRI, "토", SAT, "일", SUN
    );

    public static DayCode fromKo(String s) {
        DayCode d = KO_MAP.get(s);
        if (d == null) throw new IllegalArgumentException("지원하지 않는 요일: " + s);
        return d;
    }
    public static DayCode fromCode(int code) {
        for (DayCode d : values()) if (d.code == code) return d;
        throw new IllegalArgumentException("잘못된 요일 코드: " + code);
    }
}
