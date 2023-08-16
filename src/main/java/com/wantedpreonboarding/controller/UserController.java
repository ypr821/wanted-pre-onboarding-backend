package com.wantedpreonboarding.controller;

import com.wantedpreonboarding.dto.request.UserLoginPostRequest;
import com.wantedpreonboarding.dto.request.UserPostRequest;
import com.wantedpreonboarding.dto.response.UserLoginPostResponse;
import com.wantedpreonboarding.dto.response.UserPostResponse;
import com.wantedpreonboarding.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(value = "회원 관리 API")
@Controller
@RequestMapping(produces = "application/json; charset=utf8")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "회원 가입")
    @PostMapping(value = "api/users")
    public ResponseEntity<UserPostResponse> joinUser(HttpServletRequest request,
            @Valid @RequestBody UserPostRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.joinUser(dto));
    }

    @ApiOperation(value = "로그인")
    @PostMapping(value = "/api/users/login")
    public ResponseEntity<UserLoginPostResponse> loginUser(HttpServletRequest request,
            @Valid @RequestBody UserLoginPostRequest dto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.loginUser(dto));
    }
}
