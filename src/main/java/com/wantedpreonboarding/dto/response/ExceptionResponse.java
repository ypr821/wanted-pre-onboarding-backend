package com.wantedpreonboarding.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
public class ExceptionResponse {

    @ApiModelProperty(value = "시간", example = "2023-08-12 11:02:49.000")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime dateTime;

    @ApiModelProperty(value = "응답 상태", example = "BAD_REQUEST")
    private int status;

    @ApiModelProperty(value = "메시지", example = "등록완료")
    private String message;
}
