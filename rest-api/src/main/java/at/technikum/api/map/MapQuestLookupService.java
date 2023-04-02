package at.technikum.api.map;

import at.technikum.api.Configuration.VaultConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.CompletableFuture;

@Service
public class MapQuestLookupService {
    private static final Logger logger = LoggerFactory.getLogger(MapQuestLookupService.class);
    private final RestTemplate restTemplate;
    private final VaultConfiguration vaultConfiguration;
    @Autowired
    public MapQuestLookupService(RestTemplateBuilder restTemplateBuilder, VaultConfiguration vaultConfiguration) {
        this.restTemplate = restTemplateBuilder.build();
        this.vaultConfiguration = vaultConfiguration;
    }

    @Async
    public CompletableFuture<MapResult> findRoute(String from, String to, String transportMode) {
        logger.info("Searching Route from: {} to: {}", from, to);
        String baseURL = vaultConfiguration.getApiUrl();
        String apiKey = vaultConfiguration.getApiKeyMap();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseURL)
                .queryParam("key", apiKey)
                .queryParam("from", from)
                .queryParam("to", to)
                .queryParam("transportMode", transportMode);

        // TOOD: add error handling
        if(transportMode.equals("BICYCLE")) {
            builder.queryParam("routeType", "bicycle");
        } else if(transportMode.equals("WALKING")) {
            builder.queryParam("routeType", "pedestrian");
        }

        MapResult results = restTemplate.getForObject(builder.toUriString(), MapResult.class);
        return CompletableFuture.completedFuture(results);
    }
}
