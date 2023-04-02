package at.technikum.api.map;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Point {
    @JsonProperty("lat")
    private Double lat;
    @JsonProperty("lng")
    private Double lng;
}
