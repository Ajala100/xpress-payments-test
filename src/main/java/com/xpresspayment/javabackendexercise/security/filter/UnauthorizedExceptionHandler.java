package com.xpresspayment.javabackendexercise.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xpresspayment.javabackendexercise.security.payload.AuthenticationResponsePayload;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
public class UnauthorizedExceptionHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("Unauthorized error: {}", authException.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        AuthenticationResponsePayload errorDetails = new AuthenticationResponsePayload(false,
                "Authentication credentials is missing or invalid", HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now().toString());

        ObjectMapper mapper = new ObjectMapper();
        String errorDetailsJson = mapper.writeValueAsString(errorDetails);

        response.getWriter().write(errorDetailsJson);
    }
}
