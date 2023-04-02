package at.technikum.api.map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;

@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@Getter
@Setter
public class MapResult {
    private String sessionId;
    private Integer realTime;
    private Double distance;
    private BoundingBox boundingBox;
    @JsonProperty("info")
    private Info info;
    @JsonProperty("route")
    private void unpackRoute(Route route) {
        this.sessionId = route.getSessionId();
        this.realTime = route.getRealTime();
        this.distance = route.getDistance();
        this.boundingBox = route.getBoundingBox();
    }
}
