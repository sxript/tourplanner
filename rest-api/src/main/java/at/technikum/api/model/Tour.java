package at.technikum.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tour")
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
}
