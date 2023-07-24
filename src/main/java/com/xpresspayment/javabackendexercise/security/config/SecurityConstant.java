package com.xpresspayment.javabackendexercise.security.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class SecurityConstant {
    @Value("${xpress_payments_jwt_secret}")
    private String jwtSecret;
    @Value("${xpress_payments_jwt_token_header}")
    private String jwtTokenHeader;
    @Value("${xpress_payments_jwt_header_prefix}")
    private String tokenHeaderPrefix;
    @Value("${xpress_payments_jwt_header}")
    private String jwtHeader;
    @Value("${xpress_payments_login_uri}")
    private String loginUri;

}
