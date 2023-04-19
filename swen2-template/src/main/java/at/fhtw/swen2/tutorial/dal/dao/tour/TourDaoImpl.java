package at.fhtw.swen2.tutorial.dal.dao.tour;

import at.fhtw.swen2.tutorial.exception.BadStatusException;
import at.fhtw.swen2.tutorial.model.Tour;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class TourDaoImpl implements TourDao {
    private static final String API_BASE_URL = "http://localhost:8080/api/v1";
    private static final String API_TOURS_ENDPOINT = "/tours";
    private final WebClient webClient;

    public TourDaoImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(API_BASE_URL).build();
    }

    @Override
    public Flux<Tour> findAll() {
        // TODO: use this retry everywhere
        return webClient.get()
                .uri(API_TOURS_ENDPOINT)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new BadStatusException("Failed to retrieve tours")))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new BadStatusException("Server Error")))
                .bodyToFlux(Tour.class)
                .doOnNext(tour -> log.info("Found tour: {}", tour))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .maxBackoff(Duration.ofSeconds(10))
                        .jitter(0.5)
                        .doBeforeRetry(retrySignal -> log.warn("Retrying due to {}", retrySignal.failure().getMessage())))
                .onErrorResume(error -> {
                    log.error("Failed to retrieve tours", error);
                    return Flux.empty();
                });
    }

    @Override
    public Mono<Tour> findById(Long id) {
        return webClient.get()
                .uri(API_TOURS_ENDPOINT + "/" + id)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new BadStatusException("Failed to retrieve tour")))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new BadStatusException("Server Error")))
                .bodyToMono(Tour.class)
                .doOnSuccess(tour -> log.info("Found tour with id {}", id))
                .onErrorResume(error -> {
                    log.error("Failed to retrieve tour with id {}", id, error);
                    return Mono.empty();
                });
    }

    @Override
    public Mono<Tour> update(Tour tour) {
        return webClient.put()
                .uri(API_TOURS_ENDPOINT + "/" + tour.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(tour), Tour.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new BadStatusException("Failed to update tour")))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new BadStatusException("Server Error")))
                .bodyToMono(Tour.class)
                .doOnSuccess(updatedTour -> log.info("Updated tour with id {}", tour.getId()))
                .onErrorResume(error -> {
                    log.error("Failed to update tour with id {}", tour.getId(), error);
                    return Mono.empty();
                });
    }

    @Override
    public Mono<Tour> save(Tour entity) {
        log.info("Saving tour: {}", entity);
        return webClient.post()
                .uri(API_TOURS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(entity), Tour.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new BadStatusException("Failed to save tour")))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new BadStatusException("Server Error")))
                .bodyToMono(Tour.class)
                .doOnSuccess(createdTour -> log.info("Created tour with id {}", createdTour.getId()))
                .doOnError(error -> {
                    log.error("Failed to save tour", error);
                    throw new RuntimeException(error);
                });
    }

    @Override
    public Mono<Void> delete(Tour entity) {
        return webClient.delete()
                .uri(API_TOURS_ENDPOINT + "/" + entity.getId())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new BadStatusException("Failed to delete tour")))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new BadStatusException("Server Error")))
                .bodyToMono(Void.class)
                .doOnSuccess(response -> {
                    log.info("Deleted tour with id {}", entity.getId());
                    deleteImage(entity);
                })
                .onErrorResume(error -> {
                    log.error("Failed to delete tour with id {}", entity.getId(), error);
                    throw new RuntimeException("Failed to delete Tour with id: " + entity.getId());
                });
    }

    private void deleteImage(Tour entity) {
        Path imageDir = Paths.get("swen2-template", "src", "main", "resources", "static", "map", "images");
        String imageDirPath = imageDir.toAbsolutePath().toString();
        File outputFile = new File(imageDirPath, entity.getId() + ".jpg");
        CompletableFuture.runAsync(() -> {
            int retries = 5;
            int timeout = 1;
            while (retries > 0) {
                if (outputFile.delete()) {
                    return;
                } else log.info("Failed to delete image, retrying in {} seconds", timeout);
                try {
                    TimeUnit.SECONDS.sleep(timeout);
                    timeout *= 2;
                    retries--;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
