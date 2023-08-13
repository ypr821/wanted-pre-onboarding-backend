package com.wantedpreonboarding.dto.request;

import static com.wantedpreonboarding.common.utils.MessageConstants.REQUIRED_EMAIL;
import static com.wantedpreonboarding.common.utils.MessageConstants.REQUIRED_PW;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@ApiModel
public class UserLoginPostRequest {
    @NotBlank(message = REQUIRED_EMAIL)
    @Email(message = "이메일 주소를 확인해주세요.")
    @ApiModelProperty(name = "이메일", example = "abc123@naver.com", required = true)
    String email;

    @NotBlank(message = REQUIRED_PW)
    @Size(min = 8, message = "비밀번호를 8자이상 입력해주세요.")
    @ApiModelProperty(name = "비밀번호", example = "abcd1234", required = true)
    String password;
}
