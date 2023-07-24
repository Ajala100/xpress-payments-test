package com.xpresspayment.javabackendexercise.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xpresspayment.javabackendexercise.payload.request.LoginRequest;
import com.xpresspayment.javabackendexercise.security.config.SecurityConstant;
import com.xpresspayment.javabackendexercise.security.payload.AuthenticationResponsePayload;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.LocalDate;

@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final  AuthenticationManager authenticationManager;
    private final JWTTokenGenerator jwtTokenGenerator;
    private final ObjectMapper objectMapper;
    private final SecurityConstant securityConstant;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));

        } catch (IOException e) {
           throw new BadCredentialsException("Invalid authentication credential format");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String accessToken = jwtTokenGenerator.generateToken(authResult);
        response.setHeader(securityConstant.getJwtTokenHeader(), accessToken);
        response.getWriter().write(convertAuthenticationResponsePayloadToJson(new AuthenticationResponsePayload(true, "Login successful",
                HttpStatus.OK.value(), LocalDate.now().toString())));

    }

    private String convertAuthenticationResponsePayloadToJson(AuthenticationResponsePayload responsePayload) throws JsonProcessingException {
        if (responsePayload == null) {
            return "";
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(responsePayload);
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return request.getRequestURI().equals(securityConstant.getLoginUri());
    }


}
