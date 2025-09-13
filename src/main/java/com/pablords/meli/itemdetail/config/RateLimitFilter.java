package com.pablords.meli.itemdetail.config;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class RateLimitFilter extends OncePerRequestFilter {

    private final ConcurrentHashMap<String, RateLimiter> rateLimiters = new ConcurrentHashMap<>();
    private final RateLimiterRegistry rateLimiterRegistry;

    public RateLimitFilter() {
        // Configuração específica por IP
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(30) // 30 requests por período
                .limitRefreshPeriod(Duration.ofMinutes(1)) // Período de 1 minuto
                .timeoutDuration(Duration.ofSeconds(1)) // Timeout de 1s para obter permissão
                .build();

        this.rateLimiterRegistry = RateLimiterRegistry.of(config);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String clientIP = getClientIP(request);
        
        // Pula rate limiting para health checks e actuator
        if (shouldSkipRateLimit(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        RateLimiter rateLimiter = rateLimiters.computeIfAbsent(clientIP, 
            ip -> rateLimiterRegistry.rateLimiter("ip-" + ip));

        boolean hasPermission = rateLimiter.acquirePermission();
        
        if (!hasPermission) {
            log.warn("Rate limit exceeded for IP: {} on path: {}", clientIP, request.getRequestURI());
            
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write(String.format(
                "{\"error\": \"Too Many Requests\", \"message\": \"Rate limit exceeded for IP %s. Try again later.\", \"status\": 429}",
                clientIP
            ));
            return;
        }

        log.debug("Request allowed for IP: {} on path: {}", clientIP, request.getRequestURI());
        filterChain.doFilter(request, response);
    }

    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIP = request.getHeader("X-Real-IP");
        if (xRealIP != null && !xRealIP.isEmpty()) {
            return xRealIP;
        }
        
        return request.getRemoteAddr();
    }

    private boolean shouldSkipRateLimit(String requestURI) {
        return requestURI.startsWith("/actuator") || 
               requestURI.equals("/healthz") || 
               requestURI.equals("/readyz") ||
               requestURI.equals("/favicon.ico");
    }
}