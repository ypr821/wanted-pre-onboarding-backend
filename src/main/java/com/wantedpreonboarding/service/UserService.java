package com.wantedpreonboarding.service;

import static com.wantedpreonboarding.common.utils.MessageConstants.INVALID_EMAIL_PW;
import static com.wantedpreonboarding.common.utils.MessageConstants.INVALID_USER_ID;

import com.wantedpreonboarding.common.exception.BadRequestException;
import com.wantedpreonboarding.common.security.JwtTokenProvider;
import com.wantedpreonboarding.dto.request.UserLoginPostRequest;
import com.wantedpreonboarding.dto.request.UserPostRequest;
import com.wantedpreonboarding.dto.response.UserLoginPostResponse;
import com.wantedpreonboarding.dto.response.UserPostResponse;
import com.wantedpreonboarding.entity.UserEntity;
import com.wantedpreonboarding.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    public UserPostResponse joinUser(UserPostRequest dto) {
        Optional<UserEntity> optionalUser = userRepository.findByEmailAndDeleteDtNull(
                dto.getEmail());
        if (optionalUser.isPresent()) {
            throw new BadRequestException("이미 사용중인 email 입니다.");
        }
        UserEntity user = UserEntity.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();
        userRepository.save(user);
        return UserPostResponse.builder()
                .userId(user.getUserId())
                .message("회원가입 완료")
                .build();
    }

    public UserLoginPostResponse loginUser(UserLoginPostRequest dto) {
        Optional<UserEntity> optionalUser = userRepository.findByEmailAndDeleteDtNull(
                dto.getEmail());
        if (optionalUser.isEmpty()) {
            throw new BadRequestException(INVALID_USER_ID);
        }
        UserEntity user = optionalUser.get();
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadRequestException(INVALID_EMAIL_PW);
        }
        String accessToken = jwtTokenProvider.createToken(user.getUserId(), dto.getEmail());

        return UserLoginPostResponse.builder()
                .userId(user.getUserId())
                .message("로그인 성공")
                .accessToken("Bearer " + accessToken)
                .build();
    }

}
