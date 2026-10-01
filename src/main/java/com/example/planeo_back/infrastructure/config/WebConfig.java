package com.example.planeo_back.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final PendingDeletionInterceptor pendingDeletionInterceptor;

    public WebConfig(PendingDeletionInterceptor pendingDeletionInterceptor) {
        this.pendingDeletionInterceptor = pendingDeletionInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(pendingDeletionInterceptor).excludePathPatterns("/me/account");
    }
}
