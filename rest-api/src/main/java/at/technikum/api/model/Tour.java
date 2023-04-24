package at.technikum.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tour")
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    @Column(name = "valueFrom")
    private String from;
    @NotNull
    @Column(name = "valueTo")
    private String to;
    @NotNull
    private String transportType; // maybe enum?
    @NotNull
    private String description;
    private Double distance;

    private Integer estimatedTime;
    @Transient
    private String mapImage;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer popularity = 0;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer childFriendliness = 0;

    public Tour(String name,
                String from,
                String to,
                String transportType,
                String description,
                Double distance,
                Integer estimatedTime,
                String mapImage) {
        this.name = name;
        this.from = from;
        this.to = to;
        this.transportType = transportType;
        this.description = description;
        this.distance = distance;
        this.estimatedTime = estimatedTime;
        this.mapImage = mapImage;
    }

    public void computePopularity(List<TourLog> tourLogs) {
        this.popularity = tourLogs.size();
    }

    public void computeChildFriendliness(List<TourLog> tourLogs) {
        int totalDifficulty = 0;
        int totalDuration = 0;
        int totalDistance = 0;

        for (TourLog log : tourLogs) {
            totalDifficulty += log.getDifficulty().ordinal();
            totalDuration += log.getTotalTime();
            totalDistance += log.getTour().getDistance().intValue();
        }

        double difficultyScore = (double) totalDifficulty / tourLogs.size();
        double durationScore = (double) totalDuration / tourLogs.size();
        double distanceScore = (double) totalDistance / tourLogs.size();
        double childFriendlinessScore = (difficultyScore + (durationScore * 0.1) + (distanceScore * 0.01)) / 3;

        // Map the child-friendliness score to a value between 1 and 5
        double mappedScore = 5 - (childFriendlinessScore * 4);
        this.childFriendliness = (int) Math.max(1, Math.min(5, mappedScore));
    }
}
