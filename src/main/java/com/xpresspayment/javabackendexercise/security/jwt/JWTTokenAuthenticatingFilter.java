package com.xpresspayment.javabackendexercise.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xpresspayment.javabackendexercise.security.payload.AuthenticationResponsePayload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Component
public class JWTTokenAuthenticatingFilter extends OncePerRequestFilter {
    @Value("${xpress_payments_jwt_secret}")
    private String jwtSecret;
    @Value("${xpress_payments_jwt_header_prefix}")
    private String tokenPrefix;
    @Value("${xpress_payments_jwt_header}")
    private String requestHeader;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(requestHeader);

        if(authHeader == null || authHeader.isEmpty() || !authHeader.startsWith(tokenPrefix)){
            filterChain.doFilter(request, response);
            return;
        }
        String token = authHeader.substring(7);

        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(token).getBody();
            String username = String.valueOf(claims.get("username"));

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            List<String> authorities = (ArrayList<String>)claims.get("authorities");
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                    getAuthorities(authorities));

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            filterChain.doFilter(request, response);


        }catch(Exception e){
            String message = "Invalid Credentials";
            if(e instanceof ExpiredJwtException){
                message = "Authentication credentials is expired!!";
            }
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(convertObjectToJson(new AuthenticationResponsePayload(false, message, HttpStatus.UNAUTHORIZED.value(), LocalDate.now().toString())));

        }
    }

    private String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(List<String> authority){
        Collection< GrantedAuthority> authorities = new HashSet<>();
        authority.forEach((role)-> authorities.add(new SimpleGrantedAuthority(role)));
        return authorities;
    }
}
