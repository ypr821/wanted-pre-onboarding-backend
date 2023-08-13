package com.wantedpreonboarding.dto.response;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@ApiModel
public class PostPatchResponse extends PostGetResponse{
    String message;

}
