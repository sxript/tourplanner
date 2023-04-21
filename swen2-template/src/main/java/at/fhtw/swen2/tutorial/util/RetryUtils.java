package at.fhtw.swen2.tutorial.util;

import at.fhtw.swen2.tutorial.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
public class RetryUtils {
    private RetryUtils() {}
    public static <T> Flux<T> wrapWithRetry(Flux<T> flux, int maxRetries, Duration backoff) {
        return flux.retryWhen(Retry.backoff(maxRetries, backoff)
                .maxBackoff(Duration.ofSeconds(10))
                .jitter(0.5)
                .doBeforeRetry(retrySignal -> log.warn("Retrying due to {}", retrySignal.failure().getMessage()))
                .filter(WebClientException.class::isInstance)
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                    log.error("Failed to retrieve data", retrySignal.failure().getMessage());
                    return new ServiceException("Failed to retrieve data after max retries:  " + retrySignal.failure().getMessage(), HttpStatus.SERVICE_UNAVAILABLE.value());
                }));
    }

    public static <T> Mono<T> wrapWithRetry(Mono<T> mono, int maxRetries, Duration backoff) {
        return mono.retryWhen(Retry.backoff(maxRetries, backoff)
                .maxBackoff(Duration.ofSeconds(10))
                .jitter(0.5)
                .doBeforeRetry(retrySignal -> log.warn("Retrying due to {}", retrySignal.failure().getMessage()))
                .filter(WebClientException.class::isInstance)
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                    log.error("Failed to retrieve data", retrySignal.failure().getMessage());
                    return new ServiceException("Failed to retrieve data after max retries:  " + retrySignal.failure().getMessage(), HttpStatus.SERVICE_UNAVAILABLE.value());
                }));
    }
}
