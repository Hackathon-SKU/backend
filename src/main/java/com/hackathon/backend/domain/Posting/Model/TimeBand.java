// src/main/java/com/hackathon/backend/domain/Posting/Model/TimeBand.java
package com.hackathon.backend.domain.Posting.Model;

import java.time.LocalTime;
import java.util.Map;

public enum TimeBand {
    MORNING(1, "오전", LocalTime.of(9,0),  LocalTime.of(13,0)),
    AFTERNOON(2, "오후", LocalTime.of(13,0), LocalTime.of(18,0)),
    NIGHT(3, "심야",   LocalTime.of(18,0), LocalTime.of(22,0));

    private final int code;          // DB 저장용 숫자
    private final String ko;         // 한국어 표기
    private final LocalTime start;   // 기본 시작
    private final LocalTime end;     // 기본 종료

    TimeBand(int code, String ko, LocalTime start, LocalTime end) {
        this.code = code; this.ko = ko; this.start = start; this.end = end;
    }
    public int code() { return code; }
    public String ko() { return ko; }
    public LocalTime start() { return start; }
    public LocalTime end() { return end; }

    private static final Map<String, TimeBand> KO_MAP = Map.of(
            "오전", MORNING, "오후", AFTERNOON, "심야", NIGHT
    );

    public static TimeBand fromKo(String s) {
        TimeBand t = KO_MAP.get(s);
        if (t == null) throw new IllegalArgumentException("지원하지 않는 시간대: " + s);
        return t;
    }
    public static TimeBand fromCode(int code) {
        for (TimeBand t : values()) if (t.code == code) return t;
        throw new IllegalArgumentException("잘못된 시간대 코드: " + code);
    }
}
