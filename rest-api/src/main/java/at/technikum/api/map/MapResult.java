package at.technikum.api.map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@Getter
@Setter
public class MapResult {
    private String sessionId;
    private Integer realTime;
    private Double distance;
    @JsonProperty("boundingBox")
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
