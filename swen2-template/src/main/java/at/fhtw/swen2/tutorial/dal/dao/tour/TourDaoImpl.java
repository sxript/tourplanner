package at.fhtw.swen2.tutorial.dal.dao.tour;

import at.fhtw.swen2.tutorial.exception.BadStatusException;
import at.fhtw.swen2.tutorial.model.Tour;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TourDaoImpl implements TourDao {
    private static final String API_BASE_URL = "http://localhost:8080/api/v1";
    private static final String API_TOURS_ENDPOINT = "/tours";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public TourDaoImpl() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public List<Tour> findAll() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + API_TOURS_ENDPOINT))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Response status code: {}", response.statusCode());
            log.info("Response status code: {}", response.statusCode() >= 200 && response.statusCode() < 300);
            String responseBody = response.body();
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                List<Tour> tours = objectMapper.readValue(responseBody, new TypeReference<>() {
                });
                log.info("Found {} tours", tours.size());
                return tours;
            }
            log.info(request.uri().toString());
        } catch (IOException | InterruptedException e) {
            log.error("Failed to retrieve tours", e);
            throw new RuntimeException(e);
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<Tour> findById(Long id) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + API_TOURS_ENDPOINT + "/" + id))
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                Tour tour = objectMapper.readValue(responseBody, Tour.class);
                log.info("Found tour with id {}", id);
                return Optional.ofNullable(tour);
            }
        } catch (IOException | InterruptedException e) {
            log.error("Failed to retrieve tour with id {}", id, e);
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Tour> update(Tour tour) {
        try {
            String requestBody = objectMapper.writeValueAsString(tour);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + API_TOURS_ENDPOINT + "/" + tour.getId()))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                Tour updatedTour = objectMapper.readValue(responseBody, Tour.class);
                log.info("Updated tour with id {}", tour.getId());
                return Optional.ofNullable(updatedTour);
            } else {
                log.error("Failed to update tour with id {}: {}", tour.getId(), response.body());
                return Optional.empty();
            }
        } catch (IOException | InterruptedException e) {
            log.error("Failed to update tour with id {}", tour.getId(), e);
            return Optional.empty();
        }
    }

    @Override
    public Tour save(Tour entity) {
        try {
            String requestBody = objectMapper.writeValueAsString(entity);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + API_TOURS_ENDPOINT))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                String responseBody = response.body();
                Tour createdTour = objectMapper.readValue(responseBody, Tour.class);
                log.info("Created tour with id: {}", createdTour.getId());
                return createdTour;
            } else {
                log.error("Failed to create Tour: {}", response.body());
                throw new BadStatusException("Failed to create Tour: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            log.error("Failed to create Tour: {}", e.getMessage());
            throw new RuntimeException("Failed to create Tour: " + e.getMessage());
        }
    }

    @Override
    public void delete(Tour entity) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + API_TOURS_ENDPOINT + "/" + entity.getId()))
                .DELETE()
                .build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                log.info("Tour with id: {} has been deleted", entity.getId());
                deleteImage(entity);
            } else {
                log.error(response.body());
                throw new RuntimeException("Failed to delete Tour with id: " + entity.getId());
            }
        } catch (IOException | InterruptedException e) {
            log.error("Failed to delete Tour with id: {}", entity.getId(), e);
            throw new RuntimeException("Failed to delete Tour with id: " + entity.getId());
        }
    }

    private void deleteImage(Tour entity) {
        Path imageDir = Paths.get("swen2-template", "src", "main", "resources", "static", "map", "images");
        String imageDirPath = imageDir.toAbsolutePath().toString();
        File outputFile = new File(imageDirPath, entity.getId() + ".jpg");
        CompletableFuture.runAsync(() -> {
            int retries = 5;
            int timeout = 1;
            while (retries > 0) {
                    if(outputFile.delete()) {
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
