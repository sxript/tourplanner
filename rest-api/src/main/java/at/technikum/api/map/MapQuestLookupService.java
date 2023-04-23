package at.technikum.api.map;

import at.technikum.api.configuration.VaultConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;


@Service
public class MapQuestLookupService {
    private static final Logger logger = LoggerFactory.getLogger(MapQuestLookupService.class);
    private final WebClient webClient;
    private final VaultConfiguration vaultConfiguration;

    private static final String SIZE = "900,400";

    private static final Map<String, String> ROUTE_TYPES = Map.of(
            "BICYCLE", "bicycle",
            "WALKING", "pedestrian",
            "DRIVING", "fastest"
    );

    public MapQuestLookupService(WebClient.Builder webClientBuilder, VaultConfiguration vaultConfiguration) {
        this.vaultConfiguration = vaultConfiguration;
        this.webClient = webClientBuilder.baseUrl(vaultConfiguration.getApiUrl()).build();
    }

    /**
     * Get the route directions from the given start and end locations using the specified mode of transport.
     *
     * @param from          The start location.
     * @param to            The end location.
     * @param transportMode The mode of transport.
     * @return A Mono containing the MapResult of the route directions.
     */
    public Mono<MapResult> getRouteDirections(String from, String to, String transportMode) {
        Objects.requireNonNull(from, "from cannot be null");
        Objects.requireNonNull(to, "to cannot be null");
        Objects.requireNonNull(transportMode, "transportMode cannot be null");

        String routeType = ROUTE_TYPES.get(transportMode);
        if (routeType == null) {
            throw new IllegalArgumentException("Invalid transport mode: " + transportMode);
        }

        logger.info("Searching Route from: {} to: {}", from, to);

        String apiKey = vaultConfiguration.getApiKeyMap();

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/directions/v2/route")
                .queryParam("key", apiKey)
                .queryParam("from", from)
                .queryParam("to", to)
                .queryParam("transportMode", transportMode)
                .queryParam("routeType", routeType);


        return webClient.get()
                .uri(builder.build().toUri().toString())
                .retrieve()
                .bodyToMono(MapResult.class);
    }

    /**
     * Get a static map image for the given location and MapResult.
     *
     * @param to        The location to display on the map.
     * @param mapResult The MapResult containing the route directions.
     * @return A Mono containing the byte array of the static map image.
     */
    public Mono<byte[]> getStaticMap(String to, MapResult mapResult) {
        Objects.requireNonNull(to, "to cannot be null");

        if (mapResult == null) {
            return Mono.empty();
        }

        String apiKey = vaultConfiguration.getApiKeyMap();
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/staticmap/v5/map")
                .queryParam("key", apiKey)
                .queryParam("size", SIZE)
                .queryParam("defaultMarker", "marker-red-sm")
                .queryParam("session", mapResult.getSessionId())
                .queryParam("boundingBox", mapResult.getBoundingBox().toString())
                .queryParam("to", to);

        return webClient.get()
                .uri(builder.build().toUri().toString())
                .retrieve()
                .bodyToMono(byte[].class)
                .onErrorResume(WebClientException.class, e -> {
                    logger.error("Error retrieving static map: {}", e.getMessage());
                    return Mono.error(e);
                });
    }
}
