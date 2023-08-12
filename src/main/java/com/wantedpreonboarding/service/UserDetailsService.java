package com.wantedpreonboarding.service;

import static com.wantedpreonboarding.common.Utils.MessageConstants.INVALID_USER_ID;

import com.wantedpreonboarding.common.exception.BaseException;
import com.wantedpreonboarding.entity.UserEntity;
import com.wantedpreonboarding.repository.UserRepository;
import io.jsonwebtoken.Claims;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsService {
    private final UserRepository userRepository;

    public UserDetails loadUserByUserPrincipal(Claims claims) throws BaseException {
        long userId = Long.valueOf(String.valueOf(claims.get("userId")));
        UserEntity user = userRepository.findByUserIdAndDeleteDtNull(userId)
                .orElseThrow(() -> {
                    log.error(INVALID_USER_ID);
                    return new BaseException(INVALID_USER_ID);
                });

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        return new User(String.valueOf(user.getUserId()), user.getPassword(), grantedAuthorities);
    }
}
