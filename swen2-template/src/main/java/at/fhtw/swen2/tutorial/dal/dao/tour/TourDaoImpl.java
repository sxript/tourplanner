package at.fhtw.swen2.tutorial.dal.dao.tour;

import at.fhtw.swen2.tutorial.configuration.PropertyConfiguration;
import at.fhtw.swen2.tutorial.exception.BadStatusException;
import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.util.RetryUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class TourDaoImpl implements TourDao {

    private final String apiToursEndpoint;

    private final WebClient webClient;

    public TourDaoImpl(PropertyConfiguration propertyConfiguration, WebClient.Builder webClientBuilder) {
        String apiBaseUrl = propertyConfiguration.getApiBaseUrl();
        apiToursEndpoint = propertyConfiguration.getApiToursEndpoint();
        this.webClient = webClientBuilder.baseUrl(apiBaseUrl).build();
    }



    @Override
    public Flux<Tour> findBySearchQuery(Optional<String> searchQuery) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(apiToursEndpoint)
                        .queryParamIfPresent("searchQuery", Optional.ofNullable(searchQuery))
                        .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new BadStatusException("Failed to retrieve tours")))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new BadStatusException("Server Error")))
                .bodyToFlux(Tour.class)
                .doOnNext(tour -> log.info("Found tour: {}", tour))
                .transform(tourFlux -> RetryUtils.wrapWithRetry(tourFlux, 3, Duration.ofSeconds(1)))
                .onErrorResume(error -> {
                    log.error("Failed to retrieve tours", error);
                    return Flux.empty();
                });
    }

    @Override
    public Mono<Tour> findById(Long id) {
        return webClient.get()
                .uri(apiToursEndpoint + "/" + id)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new BadStatusException("Failed to retrieve tour")))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new BadStatusException("Server Error")))
                .bodyToMono(Tour.class)
                .doOnSuccess(tour -> log.info("Found tour with id {}", id))
                .transform(tourFlux -> RetryUtils.wrapWithRetry(tourFlux, 3, Duration.ofSeconds(1)))
                .onErrorResume(error -> {
                    log.error("Failed to retrieve tour with id {}", id, error);
                    return Mono.empty();
                });
    }

    @Override
    public Mono<Tour> update(Tour tour) {
        return webClient.put()
                .uri(apiToursEndpoint + "/" + tour.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(tour), Tour.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new BadStatusException("Failed to update tour")))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new BadStatusException("Server Error")))
                .bodyToMono(Tour.class)
                .doOnSuccess(updatedTour -> log.info("Updated tour with id {}", tour.getId()))
                .transform(tourFlux -> RetryUtils.wrapWithRetry(tourFlux, 3, Duration.ofSeconds(1)))
                .onErrorResume(error -> {
                    log.error("Failed to update tour with id {}", tour.getId(), error);
                    return Mono.empty();
                });
    }

    @Override
    public Mono<Tour> save(Tour entity) {
        log.info("Saving tour: {}", entity);
        return webClient.post()
                .uri(apiToursEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(entity), Tour.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new BadStatusException("Failed to save tour")))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new BadStatusException("Server Error")))
                .bodyToMono(Tour.class)
                .doOnSuccess(createdTour -> log.info("Created tour with id {}", createdTour.getId()))
                .transform(tourFlux -> RetryUtils.wrapWithRetry(tourFlux, 3, Duration.ofSeconds(1)))
                .doOnError(error -> {
                    log.error("Failed to save tour", error);
                    throw new RuntimeException(error);
                });
    }

    @Override
    public Mono<Void> delete(Tour entity) {
        return webClient.delete()
                .uri(apiToursEndpoint + "/" + entity.getId())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new BadStatusException("Failed to delete tour")))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new BadStatusException("Server Error")))
                .bodyToMono(Void.class)
                .doOnSuccess(response -> {
                    log.info("Deleted tour with id {}", entity.getId());
                    deleteImage(entity);
                })
                .transform(tourFlux -> RetryUtils.wrapWithRetry(tourFlux, 3, Duration.ofSeconds(1)))
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
