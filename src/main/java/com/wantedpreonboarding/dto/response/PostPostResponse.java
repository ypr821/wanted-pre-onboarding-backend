package com.wantedpreonboarding.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ApiModel
public class PostPostResponse extends BasicResponse {

    @ApiModelProperty(value = "게시글 고유번호", example = "123")
    Long postId;
}
