package com.wantedpreonboarding.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class JwtTokenException extends RuntimeException {

    private final String message;

}
