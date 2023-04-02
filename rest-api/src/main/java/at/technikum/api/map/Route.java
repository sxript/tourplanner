package at.technikum.api.map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Route {
    private String sessionId;
    private Integer realTime;
    private Double distance;
    private BoundingBox boundingBox;
}
