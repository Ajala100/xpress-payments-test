package com.xpresspayment.javabackendexercise.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xpresspayment.javabackendexercise.security.payload.AuthenticationResponsePayload;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("Unauthorized error: {}", accessDeniedException.getMessage());
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        AuthenticationResponsePayload errorDetails = new AuthenticationResponsePayload(false,
                "Access Denied. You are not authorized to access this resource", HttpStatus.FORBIDDEN.value(),
                LocalDateTime.now().toString());

        ObjectMapper mapper = new ObjectMapper();
        String errorDetailsJson = mapper.writeValueAsString(errorDetails);

        response.getWriter().write(errorDetailsJson);

    }
}
