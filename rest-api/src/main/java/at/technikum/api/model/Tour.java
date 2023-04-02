package at.technikum.api.model;

import jakarta.persistence.*;
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
    @Column(nullable = false)
    private String name;
    @Column(name = "valueFrom", nullable = false)
    private String from;
    @Column(name = "valueTo", nullable = false)
    private String to;
    @Column(nullable = false)
    private String transportType; // maybe enum?
    @Column(nullable = false)
    private String description;
    private Double distance;
    private Integer estimatedTime;
    private String routeInformation; // TODO: change this to be an image and add it on create of TOUR

    public Tour(String name,
                String from,
                String to,
                String transportType,
                String description,
                Double distance,
                Integer estimatedTime,
                String routeInformation) {
        this.name = name;
        this.from = from;
        this.to = to;
        this.transportType = transportType;
        this.description = description;
        this.distance = distance;
        this.estimatedTime = estimatedTime;
        this.routeInformation = routeInformation;
    }
}
