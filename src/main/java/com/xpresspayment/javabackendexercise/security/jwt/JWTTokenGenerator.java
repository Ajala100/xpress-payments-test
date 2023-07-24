package com.xpresspayment.javabackendexercise.security.jwt;

import com.xpresspayment.javabackendexercise.security.config.SecurityConstant;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
@AllArgsConstructor
public class JWTTokenGenerator {
    private SecurityConstant securityConstant;


    public String generateToken(Authentication authentication){

        SecretKey key = Keys.hmacShaKeyFor(securityConstant.getJwtSecret().getBytes(StandardCharsets.UTF_8));

        return Jwts.builder().setIssuer("Xpress Payments").setSubject("JWT TOKEN")
                .claim("username", authentication.getName())
                .claim("authorities", getAuthorities(authentication.getAuthorities()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 30000000))
                .signWith(key).compact();
    }

    private Set<String> getAuthorities(Collection<? extends GrantedAuthority> authority){
        Set<String> authorities = new HashSet<>();
        authority.forEach((role)-> authorities.add(role.getAuthority()));
        return authorities;
    }

}
