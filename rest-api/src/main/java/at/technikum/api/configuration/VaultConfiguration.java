package at.technikum.api.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class VaultConfiguration {
    @Value("${api_key_map}")
    private String apiKeyMap;

    @Value("${api_url}")
    private String apiUrl;
}
