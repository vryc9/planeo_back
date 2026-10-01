package com.example.planeo_back.application.service.security;

import com.example.planeo_back.infrastructure.context.RequestContext;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class AuthService {

    private final RequestContext requestContext;

    public AuthService(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    public String getUsername() {
        return requestContext.getUsername();
    }

    public Optional<Instant> getReauthenticatedAt() {
        return requestContext.getReauthenticatedAt();
    }
}
