package com.wantedpreonboarding.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ApiModel
public class UserLoginPostResponse {

    @ApiModelProperty(value = "사용자 고유번호", example = "123")
    Long userId;

    @ApiModelProperty(value = "메시지", example = "등록완료")
    String message;

    @ApiModelProperty(value = "JWT 토큰", example = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0b2tlbiIsInVzZXJJZCI6MzIsImVtYWlsIjoidGVzdDgyMUBnYW1pbC5jb20iLCJpYXQiOjE2OTE4MDU2NzMsImV4cCI6MTY5MTg0ODg3M30.ygbj9pghjKKrTfjolue4k3F2afj5OiFUBR8RFwwI7CU")
    String accessToken;
}
