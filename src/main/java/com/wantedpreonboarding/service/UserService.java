package com.wantedpreonboarding.service;

import com.wantedpreonboarding.common.exception.BadRequestException;
import com.wantedpreonboarding.common.security.JwtTokenProvider;
import com.wantedpreonboarding.dto.request.UserPostRequest;
import com.wantedpreonboarding.dto.response.UserPostResponse;
import com.wantedpreonboarding.entity.UserEntity;
import com.wantedpreonboarding.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
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
    private final AuthenticationManager authenticationManager;


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

}
