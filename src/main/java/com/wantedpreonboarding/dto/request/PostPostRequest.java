package com.wantedpreonboarding.dto.request;

import static com.wantedpreonboarding.common.utils.MessageConstants.REQUIRED_CONTENT;
import static com.wantedpreonboarding.common.utils.MessageConstants.REQUIRED_TITLE;
import static com.wantedpreonboarding.common.utils.MessageConstants.TITLE_TEXT_MAX_200;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
public class PostPostRequest {
    @NotBlank(message = REQUIRED_TITLE)
    @Size(max = 200, message = TITLE_TEXT_MAX_200)
    @ApiModelProperty(name = "제목", example = "테스트 제목", required = true)
    String title;

    @NotBlank(message = REQUIRED_CONTENT)
    @ApiModelProperty(name = "내용", example = "테스트 내용", required = true)
    String content;

}
