package at.fhtw.swen2.tutorial.configuration;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class PropertyConfiguration {
    @Value("${api.baseUrl}")
    private String apiBaseUrl;

    @Value("${api.tours.endpoint}")
    private String apiToursEndpoint;

    @Value("${api.tourlogs.endpoint}")
    private String apiTourLogsEndpoint;
}
