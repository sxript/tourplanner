package at.fhtw.swen2.tutorial.dal.dao.tourLog;

import at.fhtw.swen2.tutorial.exception.BadStatusException;
import at.fhtw.swen2.tutorial.model.TourLog;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
public class TourLogDaoImpl implements TourLogDao {
    private static final String API_BASE_URL = "http://localhost:8080/api/v1";
    private static final String API_TOURS_ENDPOINT = "/tours";
    private static final String API_TOURLOGS_ENDPOINT = "/logs";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public TourLogDaoImpl() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }


    @Override
    public List<TourLog> findAll() {
        return null;
    }

    @Override
    public Optional<TourLog> findById(Long id) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + API_TOURLOGS_ENDPOINT + "/" + id))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                TourLog tourLog = objectMapper.readValue(responseBody, TourLog.class);
                log.info("Found tourLog with id {}", id);
                return Optional.ofNullable(tourLog);
            }
        } catch (IOException | InterruptedException e) {
            log.error("Failed to retrieve tourLog with id {}", id, e);
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<TourLog> update(TourLog entity) {
        try {
            String requestBody = objectMapper.writeValueAsString(entity);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + API_TOURLOGS_ENDPOINT + "/" + entity.getId()))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                TourLog tourLog = objectMapper.readValue(responseBody, TourLog.class);
                log.info("Updated tourLog with id {}", entity.getId());
                return Optional.ofNullable(tourLog);
            } else {
                log.error("Failed to update tourLog with id {}: {}", entity.getId(), responseBody);
                throw new BadStatusException("Failed to update tourLog with id " + entity.getId());
            }
        } catch (IOException | InterruptedException e) {
            log.error("Failed to update tourLog with id {}", entity.getId(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public TourLog save(TourLog entity) {
        return null;
    }

    @Override
    public void delete(TourLog entity) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + API_TOURLOGS_ENDPOINT + "/" + entity.getId()))
                .DELETE()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                log.info("Deleted tourLog with id {}", entity.getId());
            }
        } catch (IOException | InterruptedException e) {
            log.error("Failed to delete tourLog with id {}", entity.getId(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<TourLog> findAllTourLogsByTourId(Long tourId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + API_TOURS_ENDPOINT + "/" + tourId + API_TOURLOGS_ENDPOINT))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                List<TourLog> tourLogs = objectMapper.readValue(responseBody, new TypeReference<>() {
                });
                log.info("Found {} tourLogs for tour with id {}", tourLogs.size(), tourId);
                return tourLogs;
            }
        } catch (IOException | InterruptedException e) {
            log.error("Failed to retrieve tourLogs", e);
            throw new RuntimeException(e);
        }
        return Collections.emptyList();
    }

    @Override
    public TourLog saveTourLog(Long tourId, TourLog tourLog) {
        try {
            String requestBody = objectMapper.writeValueAsString(tourLog);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + API_TOURS_ENDPOINT + "/" + tourId + API_TOURLOGS_ENDPOINT))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                TourLog savedTourLog = objectMapper.readValue(responseBody, TourLog.class);
                log.info("Saved tourLog with id {}", savedTourLog.getId());
                return savedTourLog;
            } else {
                log.error("Failed to save tourLog: {}", responseBody);
                throw new BadStatusException("Failed to save tourLog");
            }
        } catch (IOException | InterruptedException e) {
            log.error("Failed to save tourLog", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAllTourLogsByTourId(Long tourId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + API_TOURS_ENDPOINT + "/" + tourId + API_TOURLOGS_ENDPOINT))
                .DELETE()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                log.info("Deleted all tourLogs for tour with id {}", tourId);
            }
        } catch (IOException | InterruptedException e) {
            log.error("Failed to delete all tourLogs for tour with id {}", tourId, e);
            throw new RuntimeException(e);
        }
    }
}
