package at.technikum.api.map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.geo.Point;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class BoundingBox {
   @JsonProperty("ul")
   private Point ul;
   @JsonProperty("lr")
   private Point lr;
}
