package com.example.springsecurity.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class AuthController {


    private final JwtEncoder jwtEncoder;

    public AuthController(JwtEncoder jwtEncoder){
        this.jwtEncoder = jwtEncoder;
    }

    @PostMapping("/auth")
    public Authentication login(Authentication authentication){
        return authentication;
//        return new LoginResponse(createToken());
    }


    @PostMapping("/login-v2")
    public LoginRequest loginV2(@RequestBody LoginRequest loginRequest){
        System.out.println(loginRequest);
        return loginRequest;
    }

    private String createToken(){
        var claims = JwtClaimsSet.builder()
                .issuer("ORANGE_CTI0_DSC_CAIRO")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60 * 30))
                .claim("ROLES", "ADMIN USER")
                .build();
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(claims);
        return jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }

    private String createScope(Authentication authentication){
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
    }
}

record LoginResponse(String token){}

record LoginRequest(String username, String password){}
