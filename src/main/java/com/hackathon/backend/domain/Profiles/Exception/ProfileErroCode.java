package com.hackathon.backend.domain.Profiles.Exception;

import com.hackathon.backend.global.Exception.Model.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;



@Getter
@RequiredArgsConstructor
public enum ProfileErroCode implements BaseErrorCode{
    INVALID_ROLE("Profile_401_001", "다른 Role의 정보를 가져올 수 없습니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;


}
