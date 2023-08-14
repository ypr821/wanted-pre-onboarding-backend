package com.wantedpreonboarding.controller;

import static com.wantedpreonboarding.common.utils.MessageConstants.COMPLETION_POST_DELETE;
import static com.wantedpreonboarding.common.utils.MessageConstants.COMPLETION_POST_SAVED;
import static com.wantedpreonboarding.common.utils.MessageConstants.INVALID_POST_ID;
import static com.wantedpreonboarding.common.utils.MessageConstants.NO_DELETE_PERMISSION_POST;
import static com.wantedpreonboarding.common.utils.MessageConstants.NO_UPDATE_PERMISSION_POST;
import static com.wantedpreonboarding.common.utils.MessageConstants.REQUIRED_CONTENT;
import static com.wantedpreonboarding.common.utils.MessageConstants.REQUIRED_TITLE;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wantedpreonboarding.dto.request.PostPatchRequest;
import com.wantedpreonboarding.dto.request.PostPostRequest;
import com.wantedpreonboarding.entity.PostEntity;
import com.wantedpreonboarding.entity.UserEntity;
import com.wantedpreonboarding.repository.PostRepository;
import com.wantedpreonboarding.repository.UserRepository;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm:ss.SSS");

    @Test
    @WithMockUser(username = "1")
    @DisplayName(value = "게시글 등록 성공 테스트")
    void writePostSuccessTest() throws Exception {
        // given
        PostPostRequest requestDto = PostPostRequest.builder()
                .title("test 제목 입력")
                .content("test 내용 입력!")
                .build();
        String json = objectMapper.writeValueAsString(requestDto);

        // when
        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andDo(print())
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").isNumber())
                .andExpect(jsonPath("$.message").value(COMPLETION_POST_SAVED))
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()));
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName(value = "게시글 등록 실패 테스트 - 제목 누락 케이스")
    void writePostWithoutTitleFailTest() throws Exception {
        // given
        PostPostRequest requestDto = PostPostRequest.builder()
                .content("test 내용 입력!")
                .build();
        String json = objectMapper.writeValueAsString(requestDto);

        // when
        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andDo(print())
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(REQUIRED_TITLE))
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName(value = "게시글 등록 실패 테스트 - 내용 누락 케이스")
    void writePostWithoutContentFailTest() throws Exception {
        // given
        PostPostRequest requestDto = PostPostRequest.builder()
                .title("test 제목 입력!")
                .build();
        String json = objectMapper.writeValueAsString(requestDto);

        // when
        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andDo(print())
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(REQUIRED_CONTENT))
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));
    }


    @Test
    @WithMockUser(username = "1")
    @DisplayName(value = "게시글 목록 조회 성공 테스트")
    void getPostsSuccessTest() throws Exception {
        // given
        UserEntity user = UserEntity.builder()
                .email("testemail@gmail.com")
                .password("12345678")
                .build();
        userRepository.save(user);
        PostEntity post = PostEntity.builder()
                .title("테스트 제목 입력 - 게시글 목록 조회 성공 테스트")
                .content("테스트 내용 입력 - 게시글 목록 조회 성공 테스트")
                .user(user)
                .build();
        postRepository.save(post);
        // when
        mockMvc.perform(get("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content[0].postId").isNumber())
                .andExpect(jsonPath("$.content[0].userId").isNumber())
                .andExpect(jsonPath("$.content[0].email").isString())
                .andExpect(jsonPath("$.content[0].title").isString())
                .andExpect(jsonPath("$.content[0].createDt").isNotEmpty());
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName(value = "게시글 상세 조회 성공 테스트")
    void getPostSuccessTest() throws Exception {
        // given
        UserEntity user = UserEntity.builder()
                .email("testemail@gmail.com")
                .password("12345678")
                .build();
        userRepository.save(user);
        String title = "테스트 제목 입력 - 게시글 목록 조회 성공 테스트";
        String content = "테스트 내용 입력 - 게시글 목록 조회 성공 테스트";
        PostEntity post = PostEntity.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
        postRepository.save(post);
        long postId = post.getPostId();

        String createDt = post.getCreateDt().format(formatter);
        String updateDt = post.getCreateDt().format(formatter);
        // when
        mockMvc.perform(get("/api/posts/" + postId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(postId))
                .andExpect(jsonPath("$.userId").value(user.getUserId()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.createDt").value(createDt))
                .andExpect(jsonPath("$.updateDt").value(updateDt));
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName(value = "게시글 상세 조회 실패 테스트 - 유효하지 않은 게시글 고유 번호 입력한 케이스")
    void getPostFailTest() throws Exception {
        // given
        long postId = -1;
        // when
        mockMvc.perform(get("/api/posts/" + postId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(INVALID_POST_ID))
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName(value = "게시글 수정 성공 테스트")
    void updatePostSuccessTest() throws Exception {
        // given
        long userId = 1;
        UserEntity user = userRepository.findById(userId).orElseGet(() -> {
            UserEntity userEntity = UserEntity.builder()
                    .userId(1)
                    .email("testemail@gmail.com")
                    .password("12345678")
                    .build();
            return userRepository.save(userEntity);
        });
        if (user.getDeleteDt() != null) {
            user.setDeleteDt(null);
            userRepository.save(user);
        }
        PostEntity post = PostEntity.builder()
                .title("임시 제목 입력")
                .content("임시 내용 입력")
                .user(user)
                .build();
        postRepository.save(post);
        long postId = post.getPostId();
        String title = "테스트 제목 입력 - 게시글 수정 성공 테스트";
        String content = "테스트 내용 입력 - 게시글 수정 성공 테스트";
        PostPatchRequest requestDto = PostPatchRequest.builder()
                .title(title)
                .content(content)
                .build();
        String json = objectMapper.writeValueAsString(requestDto);
        // when
        mockMvc.perform(patch("/api/posts/" + postId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andDo(print())
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(postId))
                .andExpect(jsonPath("$.userId").value(user.getUserId()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.content").value(content))
                // 처음 insert 할때 updateDt 값은 createDt 값과 동일
                .andExpect(jsonPath("$.updateDt").value(Matchers.not(post.getCreateDt())));

    }

    @Test
    @WithMockUser(username = "-1")
    @DisplayName(value = "게시글 수정 실패 테스트 - 수정 접근 없는 케이스")
    void updatePostFailTest() throws Exception {
        // given
        UserEntity user = UserEntity.builder()
                .email("testemail@gmail.com")
                .password("12345678")
                .build();
        userRepository.save(user);

        PostEntity post = PostEntity.builder()
                .title("임시 제목 입력")
                .content("임시 내용 입력")
                .user(user)
                .build();
        postRepository.save(post);
        long postId = post.getPostId();
        String title = "테스트 제목 입력 - 게시글 수정 성공 테스트";
        String content = "테스트 내용 입력 - 게시글 수정 성공 테스트";
        PostPatchRequest requestDto = PostPatchRequest.builder()
                .title(title)
                .content(content)
                .build();
        String json = objectMapper.writeValueAsString(requestDto);
        // when
        mockMvc.perform(patch("/api/posts/" + postId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andDo(print())
                // then
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value(NO_UPDATE_PERMISSION_POST))
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()));

    }

    @Test
    @WithMockUser(username = "1")
    @DisplayName(value = "게시글 삭제 성공 테스트")
    void deletePostSuccessTest() throws Exception {
        // given
        long userId = 1;
        UserEntity user = userRepository.findById(userId).orElseGet(() -> {
            UserEntity userEntity = UserEntity.builder()
                    .userId(1)
                    .email("testemail@gmail.com")
                    .password("12345678")
                    .build();
            return userRepository.save(userEntity);
        });
        if (user.getDeleteDt() != null) {
            user.setDeleteDt(null);
            userRepository.save(user);
        }
        PostEntity post = PostEntity.builder()
                .title("임시 제목 입력")
                .content("임시 내용 입력")
                .user(user)
                .build();
        postRepository.save(post);
        long postId = post.getPostId();
        // when
        mockMvc.perform(delete("/api/posts/" + postId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dateTime").value(Matchers.notNullValue()))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(COMPLETION_POST_DELETE));

        Optional<PostEntity> optionalPost = postRepository.findById(postId);
        assertNotNull(optionalPost.isPresent() ? optionalPost.get().getDeleteDt() : null);
    }

    @Test
    @WithMockUser(username = "-1")
    @DisplayName(value = "게시글 삭제 실패 테스트 - 삭제 접근 없는 케이스")
    void deletePostFailTest() throws Exception {
        // given
        UserEntity user = UserEntity.builder()
                .userId(1)
                .email("testemail@gmail.com")
                .password("12345678")
                .build();
        userRepository.save(user);

        if (user.getDeleteDt() != null) {
            user.setDeleteDt(null);
            userRepository.save(user);
        }
        PostEntity post = PostEntity.builder()
                .title("임시 제목 입력")
                .content("임시 내용 입력")
                .user(user)
                .build();
        postRepository.save(post);
        long postId = post.getPostId();
        // when
        mockMvc.perform(delete("/api/posts/" + postId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                // then
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.dateTime").value(Matchers.notNullValue()))
                .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                .andExpect(jsonPath("$.message").value(NO_DELETE_PERMISSION_POST));
    }
}