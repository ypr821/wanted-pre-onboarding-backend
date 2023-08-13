package com.wantedpreonboarding.controller;

import com.wantedpreonboarding.dto.request.PostPostRequest;
import com.wantedpreonboarding.dto.response.PostPostResponse;
import com.wantedpreonboarding.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@Api(value = "게시글 관리 API")
@RequestMapping(produces = "application/json; charset=utf8")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @ApiOperation(value = "게시글 생성")
    @PostMapping(value = "/api/posts")
    public ResponseEntity<PostPostResponse> createPost(HttpServletRequest request,
            @Valid @RequestBody PostPostRequest dto, @AuthenticationPrincipal User user) {
        Long userId = Long.valueOf(user.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(postService.createPost(dto, userId));
    }

}
