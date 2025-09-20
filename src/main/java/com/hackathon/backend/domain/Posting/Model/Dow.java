package com.hackathon.backend.domain.Posting.Model;

public enum Dow {
    MON(1, "월"),
    TUE(2, "화"),
    WED(3, "수"),
    THU(4, "목"),
    FRI(5, "금"),
    SAT(6, "토"),
    SUN(7, "일");

    private final int code;     // DB에는 숫자(1..7)
    private final String ko;    // 사용자 노출용 한글

    Dow(int code, String ko) { this.code = code; this.ko = ko; }
    public int code() { return code; }
    public String ko() { return ko; }

    public static Dow from(int v) {
        for (Dow d : values()) if (d.code == v) return d;
        throw new IllegalArgumentException("Invalid day_of_week: " + v); // 1..7 외 값 방지
    }
}