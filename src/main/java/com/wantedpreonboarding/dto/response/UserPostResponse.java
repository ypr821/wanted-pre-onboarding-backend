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
public class UserPostResponse {

    @ApiModelProperty(value = "사용자 고유번호", example = "123")
    Long userId;

    @ApiModelProperty(value = "메시지", example = "가입완료")
    String message;

}
