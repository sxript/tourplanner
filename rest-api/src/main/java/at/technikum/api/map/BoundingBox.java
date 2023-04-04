package at.technikum.api.map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BoundingBox {
   @JsonProperty("ul")
   private Point ul;
   @JsonProperty("lr")
   private Point lr;

   @Override
   public String toString() {
      return ul.getLat() + "," +
              ul.getLng() + "," +
              lr.getLat() + "," +
              lr.getLng();
   }
}
