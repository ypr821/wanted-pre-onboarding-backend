package com.wantedpreonboarding.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
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
public class PostGetResponse{

    @ApiModelProperty(value = "게시글 고유번호", example = "123")
    Long postId;

    @ApiModelProperty(value = "게시글 작성자 고유번호", example = "123")
    Long userId;

    @ApiModelProperty(value = "게시글 작성자 이메일", example = "제목")
    String email;

    @ApiModelProperty(value = "게시글 제목", example = "제목")
    String title;

    @ApiModelProperty(value = "게시글 내용", example = "내용")
    String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @ApiModelProperty(value = "게시글 생성일자", example = "2023-08-12 11:02:49.000")
    LocalDateTime createDt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @ApiModelProperty(value = "게시글 마지막 수정일자", example = "2023-08-12 11:02:49.000")
    LocalDateTime updateDt;
}
