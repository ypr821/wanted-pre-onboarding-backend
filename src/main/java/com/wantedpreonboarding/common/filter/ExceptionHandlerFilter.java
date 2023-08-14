package com.wantedpreonboarding.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wantedpreonboarding.common.exception.JwtTokenException;
import com.wantedpreonboarding.dto.response.ExceptionResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtTokenException ex) {
            log.error("JwtTokenException exception handler filter");
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, ex);
        } catch (RuntimeException ex) {
            log.error("runtime exception exception handler filter");
            setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, response, ex);
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response, Throwable ex) {
        response.setStatus(status.value());
        response.setContentType("application/json; charset=utf8");
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .dateTime(LocalDateTime.now())
                .message(ex.getMessage())
                .status(status.value())
                .build();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            String json = objectMapper.writeValueAsString(exceptionResponse);
            log.info(json);
            response.getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
