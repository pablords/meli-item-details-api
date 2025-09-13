package com.pablords.meli.itemdetail.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pablords.meli.itemdetail.domain.application.exception.NotFoundException;

import java.time.Duration;

@Configuration
@Slf4j
public class ResilienceConfig {

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(50) // 50% de falhas para abrir o circuito
                .waitDurationInOpenState(Duration.ofSeconds(30)) // Espera 30s antes de tentar novamente
                .slidingWindowSize(10) // Janela de 10 requests para calcular taxa de falha
                .minimumNumberOfCalls(5) // Mínimo de 5 calls antes de calcular taxa
                .slowCallRateThreshold(50) // 50% de calls lentas para considerar falha
                .slowCallDurationThreshold(Duration.ofSeconds(2)) // Calls > 2s são consideradas lentas
                .permittedNumberOfCallsInHalfOpenState(3) // Permite 3 calls no estado half-open
                // Ignora exceções de negócio - Circuit Breaker não deve interferir em erros de domínio
                .ignoreExceptions(NotFoundException.class)
                .build();

        return CircuitBreakerRegistry.of(config);
    }

    @Bean
    public CircuitBreaker productServiceCircuitBreaker(CircuitBreakerRegistry registry) {
        return registry.circuitBreaker("productService");
    }

    @Bean
    public RateLimiterRegistry rateLimiterRegistry() {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(100) // 100 requests por período
                .limitRefreshPeriod(Duration.ofMinutes(1)) // Período de 1 minuto
                .timeoutDuration(Duration.ofSeconds(5)) // Timeout de 5s para obter permissão
                .build();

        return RateLimiterRegistry.of(config);
    }

    @Bean
    public RateLimiter globalRateLimiter(RateLimiterRegistry registry) {
        return registry.rateLimiter("global");
    }
}