package at.fhtw.swen2.tutorial.dal.dao.tourLog;

import at.fhtw.swen2.tutorial.exception.BadStatusException;
import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.model.TourLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class TourLogDaoImpl implements TourLogDao {
    // TODO: remove this and move all into service
    private static final String API_BASE_URL = "http://localhost:8080/api/v1";
    private static final String API_TOURS_ENDPOINT = "/tours";
    private static final String API_TOURLOGS_ENDPOINT = "/logs";
    private final WebClient webClient;

    public TourLogDaoImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(API_BASE_URL).build();
    }


    @Override
    public Flux<Tour> findAll() {
        return null;
    }

    @Override
    public Mono<TourLog> findById(Long id) {
        return webClient.get()
                .uri(API_TOURLOGS_ENDPOINT + "/" + id)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new BadStatusException("Failed to retrieve tourLog with id " + id)))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new BadStatusException("Server error")))
                .bodyToMono(TourLog.class)
                .doOnSuccess(tourLog -> log.info("Found tourLog with id {}", id))
                .onErrorResume(error -> {
                    log.error("Failed to retrieve tourLog with id {}", id, error);
                    return Mono.empty();
                });
    }

    @Override
    public Mono<TourLog> update(TourLog entity) {
        return webClient.put()
                .uri(API_TOURLOGS_ENDPOINT + "/" + entity.getId())
                .body(Mono.just(entity), TourLog.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new BadStatusException("Failed to update tourLog with id " + entity.getId())))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new BadStatusException("Server error")))
                .bodyToMono(TourLog.class)
                .doOnSuccess(tourLog -> log.info("Updated tourLog with id {}", entity.getId()))
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
                .uri(API_TOURLOGS_ENDPOINT + "/" + entity.getId())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new BadStatusException("Failed to delete tourLog with id " + entity.getId())))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new BadStatusException("Server error")))
                .bodyToMono(Void.class)
                .doOnSuccess(unused -> log.info("Deleted tourLog with id {}", entity.getId()))
                .onErrorResume(error -> {
                    log.error("Failed to delete tourLog with id {}: {}", entity.getId(), error.getMessage());
                    throw new BadStatusException("Failed to delete tourLog with id " + entity.getId());
                });
    }

    @Override
    public Mono<List<TourLog>> findAllTourLogsByTourId(Long tourId) {
        return webClient.get()
                .uri(API_BASE_URL + API_TOURS_ENDPOINT + "/" + tourId + API_TOURLOGS_ENDPOINT)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<TourLog>>() {
                })
                .doOnSuccess(tourLogs -> log.info("Found {} tourLogs for tour with id {}", tourLogs.size(), tourId))
                .switchIfEmpty(Mono.error(new BadStatusException("Failed to retrieve tourLogs for tour with id " + tourId)))
                .onErrorResume(e -> {
                    log.error("Error retrieving tourLogs for tour with id {}: {}", tourId, e.getMessage());
                    return Mono.empty();
                });
    }

    @Override
    public Mono<TourLog> saveTourLog(Long tourId, TourLog tourLog) {
        return webClient.post()
                .uri(API_BASE_URL + API_TOURS_ENDPOINT + "/" + tourId + API_TOURLOGS_ENDPOINT)
                .body(Mono.just(tourLog), TourLog.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new BadStatusException("Failed to save tourLog for tour with id " + tourId)))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new BadStatusException("Server error")))
                .bodyToMono(TourLog.class)
                .doOnSuccess(savedTourLog -> log.info("Saved tourLog with id {}", savedTourLog.getId()))
                .onErrorResume(error -> {
                    log.error("Failed to save tourLog for tour with id {}: {}", tourId, error.getMessage());
                    throw new BadStatusException("Failed to save tourLog for tour with id " + tourId);
                });
    }

    @Override
    public Mono<Void> deleteAllTourLogsByTourId(Long tourId) {
        return webClient.delete()
                .uri(API_BASE_URL + API_TOURS_ENDPOINT + "/" + tourId + API_TOURLOGS_ENDPOINT)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new BadStatusException("Failed to delete all tourLogs for tour with id " + tourId)))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new BadStatusException("Server error")))
                .bodyToMono(Void.class)
                .doOnSuccess(unused -> log.info("Deleted all tourLogs for tour with id {}", tourId))
                .onErrorResume(error -> {
                    log.error("Failed to delete all tourLogs for tour with id {}: {}", tourId, error.getMessage());
                    throw new BadStatusException("Failed to delete all tourLogs for tour with id " + tourId);
                });
    }
}
