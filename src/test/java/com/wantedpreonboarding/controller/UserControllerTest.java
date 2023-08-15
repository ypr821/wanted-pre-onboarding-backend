package com.wantedpreonboarding.controller;

import static com.wantedpreonboarding.common.utils.MessageConstants.INVALID_EMAIL_PW;
import static com.wantedpreonboarding.common.utils.MessageConstants.INVALID_USER_ID;
import static com.wantedpreonboarding.common.utils.MessageConstants.REQUIRED_EMAIL;
import static com.wantedpreonboarding.common.utils.MessageConstants.REQUIRED_PW;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wantedpreonboarding.dto.request.UserPostRequest;
import com.wantedpreonboarding.dto.request.UserLoginPostRequest;
import com.wantedpreonboarding.entity.UserEntity;
import com.wantedpreonboarding.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName(value = "회원가입 성공 테스트")
    void joinUserSuccessTest() throws Exception {
        // given
        UserPostRequest requestDto = UserPostRequest.builder()
                .email("test@gmail.com")
                .password("test1234")
                .build();
        String json = objectMapper.writeValueAsString(requestDto);

        // when
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andDo(print())
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("회원가입 완료"));
    }

    @Test
    @DisplayName(value = "회원가입 실패 테스트 - 이미 사용중인 email 을 사용한 케이스")
    void joinUserWithJoinedEmailFailTest() throws Exception {
        // given
        UserEntity user = UserEntity.builder()
                .email("test@gmail.com")
                .password("test1234").
                build();
        userRepository.save(user);
        UserPostRequest requestDto = UserPostRequest.builder()
                .email("test@gmail.com")
                .password("test1234")
                .build();
        String json = objectMapper.writeValueAsString(requestDto);
        // when
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andDo(print())
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이미 사용중인 email 입니다."));
    }

    @Test
    @DisplayName(value = "회원가입 실패 테스트 - @ 없는 email 을 사용한 케이스")
    void joinUserWithWrongEmailFailTest() throws Exception {
        // given
        UserPostRequest requestDto = UserPostRequest.builder()
                .email("testgmail.com")
                .password("test1234")
                .build();
        String json = objectMapper.writeValueAsString(requestDto);
        // when
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andDo(print())
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이메일 주소를 확인해주세요."));
    }

    @Test
    @DisplayName(value = "회원가입 실패 테스트 - 8자 미만 비밀번호를 사용한 케이스")
    void joinUserWithWrongPasswordFailTest() throws Exception {
        // given
        UserPostRequest requestDto = UserPostRequest.builder()
                .email("test@gmail.com")
                .password("test")
                .build();
        String json = objectMapper.writeValueAsString(requestDto);
        // when
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andDo(print())
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("비밀번호를 8자이상 입력해주세요."));
    }

    @Test
    @DisplayName(value = "회원가입 실패 테스트 - 이메일을 입력하지 않은 케이스")
    void joinUserWithoutEmailFailTest() throws Exception {
        // given
        UserPostRequest requestDto = UserPostRequest.builder()
                .password("test1234")
                .build();
        String json = objectMapper.writeValueAsString(requestDto);
        // when
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andDo(print())
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(REQUIRED_EMAIL));
    }

    @Test
    @DisplayName(value = "회원가입 실패 테스트 - 비밀번호를 입력하지 않은 케이스")
    void joinUserWithoutPasswordFailTest() throws Exception {
        // given
        UserPostRequest requestDto = UserPostRequest.builder()
                .email("test@gmail.com")
                .build();
        String json = objectMapper.writeValueAsString(requestDto);
        // when
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andDo(print())
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(REQUIRED_PW));
    }

/********************************************************************************************
 * ******************************************************************************************/

    @Test
    @DisplayName(value = "로그인 성공 테스트")
    void loginUserSuccessTest() throws Exception {
        // given
        String password = "12345678";
        UserEntity user = UserEntity.builder()
                .email("test@gmail.com")
                .password(passwordEncoder.encode(password))
                .build();
        userRepository.save(user);
        UserLoginPostRequest requestDto = UserLoginPostRequest.builder()
                .email(user.getEmail())
                .password(password)
                .build();
        String json = objectMapper.writeValueAsString(requestDto);
        // when
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andDo(print())
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("로그인 성공"))
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    @Test
    @DisplayName(value = "로그인 실패 테스트 - 이메일을 입력하지 않은 케이스")
    void loginUserWithoutEmailFailTest() throws Exception {
        // given
        UserLoginPostRequest requestDto = UserLoginPostRequest.builder()
                .password("12345678")
                .build();
        String json = objectMapper.writeValueAsString(requestDto);
        // when
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andDo(print())
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(REQUIRED_EMAIL));
    }

    @Test
    @DisplayName(value = "로그인 실패 테스트 - 비밀번호를 입력하지 않은 케이스")
    void loginUserWithoutPasswordFailTest() throws Exception {
        // given
        UserLoginPostRequest requestDto = UserLoginPostRequest.builder()
                .email("test@gmail.com")
                .build();
        String json = objectMapper.writeValueAsString(requestDto);
        // when
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andDo(print())
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(REQUIRED_PW));
    }

    @Test
    @DisplayName(value = "로그인 실패 테스트 - 가입하지 않은 이메일을 입력한 케이스")
    void loginUserWithNoJoinedEmailFailTest() throws Exception {
        // given
        UserLoginPostRequest requestDto = UserLoginPostRequest.builder()
                .email("test@gmail.com")
                .password("12345678")
                .build();
        String json = objectMapper.writeValueAsString(requestDto);
        // when
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andDo(print())
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(INVALID_USER_ID));
    }

    @Test
    @DisplayName(value = "로그인 실패 테스트 - 잘못된 비밀번호를 입력한 케이스")
    void loginUserWithWrongPasswordFailTest() throws Exception {
        // given
        String password = "12345678";
        UserEntity user = UserEntity.builder()
                .email("test@gmail.com")
                .password(passwordEncoder.encode(password))
                .build();
        userRepository.save(user);
        UserLoginPostRequest requestDto = UserLoginPostRequest.builder()
                .email(user.getEmail())
                .password(password + "12")
                .build();
        String json = objectMapper.writeValueAsString(requestDto);
        // when
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andDo(print())
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(INVALID_EMAIL_PW));
    }

}