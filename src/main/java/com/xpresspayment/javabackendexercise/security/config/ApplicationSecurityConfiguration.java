package com.xpresspayment.javabackendexercise.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xpresspayment.javabackendexercise.security.filter.CustomAccessDeniedHandler;
import com.xpresspayment.javabackendexercise.security.filter.UnauthorizedExceptionHandler;
import com.xpresspayment.javabackendexercise.security.jwt.CustomAuthenticationFilter;
import com.xpresspayment.javabackendexercise.security.jwt.JWTTokenAuthenticatingFilter;
import com.xpresspayment.javabackendexercise.security.jwt.JWTTokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.RequestCacheAwareFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ApplicationSecurityConfiguration {
    private final UnauthorizedExceptionHandler unauthorizedExceptionHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final SecurityConstant securityConstant;
    private final JWTTokenGenerator jwtTokenGenerator;
    private final UserDetailsService userDetailsService;
    private final JWTTokenAuthenticatingFilter jwtTokenAuthenticatingFilter;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(customAccessDeniedHandler));
        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(unauthorizedExceptionHandler));
        http.cors(httpSecurityCorsConfigurer -> corsConfiguration());
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
            authorizationManagerRequestMatcherRegistry.requestMatchers("/api/v1/users/register").permitAll();
            authorizationManagerRequestMatcherRegistry.requestMatchers("/api/v1/airtime/top-up").authenticated();
        });

        http.addFilterBefore(jwtTokenAuthenticatingFilter, RequestCacheAwareFilter.class);
        http.addFilterBefore(createCustomAuthenticationFilterObject(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfiguration corsConfiguration(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setMaxAge(200L);
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setExposedHeaders(List.of(securityConstant.getJwtHeader(), securityConstant.getJwtTokenHeader()));
        return corsConfiguration;
    }

    private CustomAuthenticationFilter createCustomAuthenticationFilterObject(){
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(createAuthenticationManagerBean(), jwtTokenGenerator, new ObjectMapper(), securityConstant);
        customAuthenticationFilter.setFilterProcessesUrl("/api/v1/auth/sign-in");
        return customAuthenticationFilter;
    }

    @Bean
    public AuthenticationManager createAuthenticationManagerBean(){
        return new ProviderManager(createAuthenticationProvider());
    }

    @Bean
    public AuthenticationProvider createAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
}
