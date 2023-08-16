package com.wantedpreonboarding.controller;

import com.wantedpreonboarding.dto.request.PostPatchRequest;
import com.wantedpreonboarding.dto.request.PostPostRequest;
import com.wantedpreonboarding.dto.response.BasicResponse;
import com.wantedpreonboarding.dto.response.PostGetResponse;
import com.wantedpreonboarding.dto.response.PostListGetResponse;
import com.wantedpreonboarding.dto.response.PostPatchResponse;
import com.wantedpreonboarding.dto.response.PostPostResponse;
import com.wantedpreonboarding.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(dto, userId));
    }

    @ApiOperation(value = "게시글 목록 조회")
    @GetMapping(value = "/api/posts")
    public ResponseEntity<Page<PostListGetResponse>> getPostList(HttpServletRequest request,
            @PageableDefault(sort = "postId", direction = Direction.DESC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostList(pageable));
    }

    @ApiOperation(value = "게시글 상세 조회")
    @GetMapping(value = "/api/posts/{id}")
    public ResponseEntity<PostGetResponse> getPost(HttpServletRequest request,
            @PathVariable(value = "id") Long postId) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPost(postId));
    }

    @ApiOperation(value = "게시글 수정")
    @PatchMapping(value = "/api/posts/{id}")
    public ResponseEntity<PostPatchResponse> updatePost(HttpServletRequest request,
            @PathVariable(value = "id") Long postId, @RequestBody PostPatchRequest dto,
            @AuthenticationPrincipal User user) {
        Long userId = Long.valueOf(user.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(postService.updatePost(postId, dto, userId));
    }

    @ApiOperation(value = "게시글 삭제")
    @DeleteMapping(value = "/api/posts/{id}")
    public ResponseEntity<BasicResponse> deletePost(HttpServletRequest request,
            @PathVariable(value = "id") Long postId, @AuthenticationPrincipal User user) {
        Long userId = Long.valueOf(user.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(postService.deletePost(postId, userId));
    }
}
