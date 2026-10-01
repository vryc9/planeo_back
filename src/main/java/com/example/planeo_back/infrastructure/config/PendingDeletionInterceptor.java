package com.example.planeo_back.infrastructure.config;

import com.example.planeo_back.domain.ports.UserAccountRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Refuses every request from an account whose deletion is in flight, which also cuts the user's
 * other sessions. DELETE /me/account is excluded (see WebConfig) so it stays idempotent.
 */
@Component
public class PendingDeletionInterceptor implements HandlerInterceptor {

    private final UserAccountRepository userAccounts;

    public PendingDeletionInterceptor(UserAccountRepository userAccounts) {
        this.userAccounts = userAccounts;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String username = request.getHeader("X-Auth-Username");
        if (username != null && userAccounts.isBlocked(username)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        return true;
    }
}
