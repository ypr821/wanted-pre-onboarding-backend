package com.wantedpreonboarding.service;

import static com.wantedpreonboarding.common.utils.MessageConstants.COMPLETION_POST_SAVED;
import static com.wantedpreonboarding.common.utils.MessageConstants.COMPLETION_POST_UPDATE;
import static com.wantedpreonboarding.common.utils.MessageConstants.INVALID_POST_ID;
import static com.wantedpreonboarding.common.utils.MessageConstants.NO_UPDATE_PERMISSION_POST;

import com.wantedpreonboarding.common.exception.BadRequestException;
import com.wantedpreonboarding.common.exception.ForbiddenException;
import com.wantedpreonboarding.dto.request.PostPatchRequest;
import com.wantedpreonboarding.dto.request.PostPostRequest;
import com.wantedpreonboarding.dto.response.PostGetResponse;
import com.wantedpreonboarding.dto.response.PostListGetResponse;
import com.wantedpreonboarding.dto.response.PostPatchResponse;
import com.wantedpreonboarding.dto.response.PostPostResponse;
import com.wantedpreonboarding.entity.PostEntity;
import com.wantedpreonboarding.repository.PostRepository;
import com.wantedpreonboarding.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostPostResponse createPost(PostPostRequest dto, Long userId) {
        PostEntity postEntity = PostEntity.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .user(userRepository.findById(userId).orElseThrow(()-> new BadRequestException("다시 로그인해주세요.")))
                .build();
        postRepository.save(postEntity);

        return PostPostResponse.builder()
                .postId(postEntity.getPostId())
                .message(COMPLETION_POST_SAVED)
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .build();
    }

    public Page<PostListGetResponse> getPostList(Pageable pageable) {
        Page<PostEntity> postEntities = postRepository.findByDeleteDtNullOrderByCreateDtDesc(pageable);
        return postEntities.map(entity -> PostListGetResponse.builder()
                .postId(entity.getPostId())
                .title(entity.getTitle())
                .createDt(entity.getCreateDt())
                .userId(entity.getUser().getUserId())
                .email(entity.getUser().getEmail())
                .build());
    }

    public PostGetResponse getPost(Long postId) {
        Optional<PostEntity> optionalPost = postRepository.findById(postId);
        PostEntity post = optionalPost.orElseThrow(() -> new BadRequestException(INVALID_POST_ID));
        return PostGetResponse.builder()
                .postId(post.getPostId())
                .userId(post.getUser().getUserId())
                .email(post.getUser().getEmail())
                .title(post.getTitle())
                .content(post.getContent())
                .createDt(post.getCreateDt())
                .updateDt(post.getUpdateDt())
                .build();
    }

    public PostPatchResponse updatePost(Long postId, PostPatchRequest dto, Long userId) {
        Optional<PostEntity> optionalPost = postRepository.findById(postId);
        PostEntity post = optionalPost.orElseThrow(() -> new BadRequestException(INVALID_POST_ID));
        if (post.getUser().getUserId() != userId) {
            throw new ForbiddenException(NO_UPDATE_PERMISSION_POST);
        }
        if (dto.getContent() != null) {
            post.setContent(dto.getContent());
        }
        if (dto.getTitle() != null) {
            post.setTitle(dto.getTitle());
        }
        postRepository.save(post);
        return PostPatchResponse.builder()
                .postId(post.getPostId())
                .userId(post.getUser().getUserId())
                .email(post.getUser().getEmail())
                .title(post.getTitle())
                .content(post.getContent())
                .createDt(post.getCreateDt())
                .updateDt(post.getUpdateDt())
                .message(COMPLETION_POST_UPDATE)
                .build();
    }
}
