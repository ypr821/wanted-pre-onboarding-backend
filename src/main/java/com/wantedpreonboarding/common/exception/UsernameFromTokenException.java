package com.wantedpreonboarding.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UsernameFromTokenException extends RuntimeException{
    private final String message;
}
