package com.wantedpreonboarding.common.filter;

import static com.wantedpreonboarding.common.utils.MessageConstants.INVALID_JWT;

import com.wantedpreonboarding.common.exception.JwtTokenException;
import com.wantedpreonboarding.common.security.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 헤더(Authorization)에 있는 토큰을 꺼내 이상이 없는 경우 SecurityContext 에 저장
 *
 * Request 이전에 작동
 */

public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(request);
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext()
                        .setAuthentication(auth); // 정상 토큰이면 SecurityContext 에 저장
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            throw new JwtTokenException(INVALID_JWT);
        }

        filterChain.doFilter(request, response);
    }
}
