package at.fhtw.swen2.tutorial.dal.dao.tourLog;

import at.fhtw.swen2.tutorial.configuration.PropertyConfiguration;
import at.fhtw.swen2.tutorial.exception.BadStatusException;
import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.model.TourLog;
import at.fhtw.swen2.tutorial.util.RetryUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class TourLogDaoImpl implements TourLogDao {
    // TODO: remove this and move all into service
    private final String apiToursEndpoint;

    private final String apiTourLogsEndpoint;

    private final WebClient webClient;

    public TourLogDaoImpl(PropertyConfiguration propertyConfiguration, WebClient.Builder webClientBuilder) {
        String apiBaseUrl = propertyConfiguration.getApiBaseUrl();
        apiToursEndpoint = propertyConfiguration.getApiToursEndpoint();
        apiTourLogsEndpoint = propertyConfiguration.getApiTourLogsEndpoint();
        this.webClient = webClientBuilder.baseUrl(apiBaseUrl).build();
    }


    @Override
    public Flux<Tour> findBySearchQuery(Optional<String> searchQuery) {
        return null;
    }

    // TODO: add global controller advice for Retry Error (maybe custom exception)?
    @Override
    public Mono<TourLog> findById(Long id) {
        return webClient.get()
                .uri(apiTourLogsEndpoint + "/" + id)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new BadStatusException("Failed to retrieve tourLog with id " + id)))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new BadStatusException("Server error")))
                .bodyToMono(TourLog.class)
                .doOnSuccess(tourLog -> log.info("Found tourLog with id {}", id))
                .transform(tourFlux -> RetryUtils.wrapWithRetry(tourFlux, 3, Duration.ofSeconds(1)))
                .onErrorResume(error -> {
                    log.error("Failed to retrieve tourLog with id {}", id, error);
                    return Mono.empty();
                });
    }

    @Override
    public Mono<TourLog> update(TourLog entity) {
        return webClient.put()
                .uri(apiTourLogsEndpoint + "/" + entity.getId())
                .body(Mono.just(entity), TourLog.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new BadStatusException("Failed to update tourLog with id " + entity.getId())))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new BadStatusException("Server error")))
                .bodyToMono(TourLog.class)
                .doOnSuccess(tourLog -> log.info("Updated tourLog with id {}", entity.getId()))
                .transform(tourFlux -> RetryUtils.wrapWithRetry(tourFlux, 3, Duration.ofSeconds(1)))
                .onErrorResume(error -> {
                    log.error("Failed to update tourLog with id {}: {}", entity.getId(), error.getMessage());
                    throw new BadStatusException("Failed to update tourLog with id " + entity.getId());
                });

    }

    @Override
    public Mono<Tour> save(TourLog entity) {
        return null;
    }

    @Override
    public Mono<Void> delete(TourLog entity) {
        return webClient.delete()
                .uri(apiTourLogsEndpoint + "/" + entity.getId())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new BadStatusException("Failed to delete tourLog with id " + entity.getId())))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new BadStatusException("Server error")))
                .bodyToMono(Void.class)
                .doOnSuccess(unused -> log.info("Deleted tourLog with id {}", entity.getId()))
                .transform(tourFlux -> RetryUtils.wrapWithRetry(tourFlux, 3, Duration.ofSeconds(1)))
                .onErrorResume(error -> {
                    log.error("Failed to delete tourLog with id {}: {}", entity.getId(), error.getMessage());
                    throw new BadStatusException("Failed to delete tourLog with id " + entity.getId());
                });
    }

    @Override
    public Mono<List<TourLog>> findAllTourLogsByTourId(Long tourId, Optional<String> searchQuery) {

        System.out.println(searchQuery);
        System.out.println(tourId);

        System.out.println(apiTourLogsEndpoint+"/"+tourId);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(apiToursEndpoint + "/" + tourId + apiTourLogsEndpoint)
                        .queryParamIfPresent("searchQuery", Optional.ofNullable(searchQuery))
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<TourLog>>() {
                })
                .doOnSuccess(tourLogs -> log.info("Found {} tourLogs for tour with id {}", tourLogs.size(), tourId))
                .switchIfEmpty(Mono.error(new BadStatusException("Failed to retrieve tourLogs for tour with id " + tourId)))
                .transform(tourFlux -> RetryUtils.wrapWithRetry(tourFlux, 3, Duration.ofSeconds(1)))
                .onErrorResume(e -> {
                    log.error("Error retrieving tourLogs for tour with id {}: {}", tourId, e.getMessage());
                    return Mono.empty();
                });
    }

    @Override
    public Mono<TourLog> saveTourLog(Long tourId, TourLog tourLog) {
        return webClient.post()
                .uri(apiToursEndpoint + "/" + tourId + apiTourLogsEndpoint)
                .body(Mono.just(tourLog), TourLog.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new BadStatusException("Failed to save tourLog for tour with id " + tourId)))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new BadStatusException("Server error")))
                .bodyToMono(TourLog.class)
                .doOnSuccess(savedTourLog -> log.info("Saved tourLog with id {}", savedTourLog.getId()))
                .transform(tourFlux -> RetryUtils.wrapWithRetry(tourFlux, 3, Duration.ofSeconds(1)))
                .onErrorResume(error -> {
                    log.error("Failed to save tourLog for tour with id {}: {}", tourId, error.getMessage());
                    throw new BadStatusException("Failed to save tourLog for tour with id " + tourId);
                });
    }

    @Override
    public Mono<Void> deleteAllTourLogsByTourId(Long tourId) {
        return webClient.delete()
                .uri(apiToursEndpoint + "/" + tourId + apiTourLogsEndpoint)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new BadStatusException("Failed to delete all tourLogs for tour with id " + tourId)))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new BadStatusException("Server error")))
                .bodyToMono(Void.class)
                .doOnSuccess(unused -> log.info("Deleted all tourLogs for tour with id {}", tourId))
                .transform(tourFlux -> RetryUtils.wrapWithRetry(tourFlux, 3, Duration.ofSeconds(1)))
                .onErrorResume(error -> {
                    log.error("Failed to delete all tourLogs for tour with id {}: {}", tourId, error.getMessage());
                    throw new BadStatusException("Failed to delete all tourLogs for tour with id " + tourId);
                });
    }
}
