package com.wantedpreonboarding.common.security;

import static com.wantedpreonboarding.common.Utils.MessageConstants.EXPIRED_JWT;
import static com.wantedpreonboarding.common.Utils.MessageConstants.INVALID_JWT;

import com.wantedpreonboarding.common.exception.BaseException;
import com.wantedpreonboarding.service.UserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class JwtTokenProvider {

    @Value("${spring.jwt.secret}")
    private String secretKey;

    @Value("${spring.jwt.token.access-expiration-time}")
    private long expirationTime;

    private final UserDetailsService userDetailsService;

    public String createToken(long userId, String email) {
        Claims claims = Jwts.claims().setSubject("token");
        claims.put("userId", userId);
        claims.put("email", email);
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * http 헤더로부터 bearer 토큰 추출
     */
    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


    /**
     * 토큰 검증
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error(EXPIRED_JWT);
            throw new BaseException(EXPIRED_JWT);
        } catch (JwtException e) {
            log.error(INVALID_JWT);
            throw new BaseException(INVALID_JWT);
        }
    }

    /**
     * 토큰으로부터 클레임을 만들고, 이를 통해 User 객체 생성해 Authentication 객체 반환
     */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser().
                setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        UserDetails userDetails = userDetailsService.loadUserByUserPrincipal(claims);

        return new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());
    }

}