package at.fhtw.swen2.tutorial.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

@Getter
@Setter
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Slf4j
public class Tour {
    private Long id;
    private String name;
    private String from;
    private String to;
    private String transportType;
    private String description;
    private Double distance;
    private Integer estimatedTime;
    private byte[] mapImage;

    @JsonProperty("mapImage")
    public void setMapImage(String mapImageString) {
        if (mapImageString == null) {
            mapImage = null;
            return;
        }
        this.mapImage = Base64.getDecoder().decode(mapImageString);
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(new ByteArrayInputStream(mapImage));
            File outputFile = new File("/map/images/" + id + ".jpg");
            ImageIO.write(bufferedImage, "jpg", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
