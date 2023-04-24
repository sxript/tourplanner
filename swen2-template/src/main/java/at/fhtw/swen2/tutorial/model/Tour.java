package at.fhtw.swen2.tutorial.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private Integer popularity;
    private Integer childFriendliness;

    // This is only used for JSON deserialization after that
    // the mapImage is stored as a file so we don't need it anymore
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
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

            // TODO: rename after app rename
            Path imageDir = Paths.get("swen2-template", "src", "main", "resources", "static", "map", "images");
            String imageDirPath = imageDir.toAbsolutePath().toString();
            File outputFile = new File(imageDirPath,  id + ".jpg");
            ImageIO.write(bufferedImage, "jpg", outputFile);
        } catch (IOException e) {
            log.error("Failed to decode map image", e);
            throw new RuntimeException(e);
        }
        mapImage = new byte[]{};
    }
}
