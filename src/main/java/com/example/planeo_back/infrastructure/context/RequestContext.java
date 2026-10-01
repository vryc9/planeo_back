package com.example.planeo_back.infrastructure.context;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.util.Optional;

@Component
public class RequestContext {
    public String getUsername() {
        ServletRequestAttributes attributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            throw new RuntimeException("No request context available");
        }

        HttpServletRequest request = attributes.getRequest();
        String username = request.getHeader("X-Auth-Username");

        if (username == null) {
            throw new RuntimeException("X-Auth-Username header missing");
        }
        return username;
    }

    /**
     * Instant of the user's last password re-confirmation, as stamped by the gateway
     * (X-Auth-Reauth-At, epoch seconds). Empty when the session was never re-confirmed.
     */
    public Optional<Instant> getReauthenticatedAt() {
        ServletRequestAttributes attributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            throw new RuntimeException("No request context available");
        }

        String value = attributes.getRequest().getHeader("X-Auth-Reauth-At");
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(Instant.ofEpochSecond(Long.parseLong(value.trim())));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
