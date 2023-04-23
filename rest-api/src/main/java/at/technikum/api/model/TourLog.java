package at.technikum.api.model;

import at.technikum.api.enums.Difficulty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "tour_logs")
public class TourLog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tour_logs_generator")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime date;

    @NotNull
    private String comment;

    @NotNull
    private Integer totalTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @NotNull
    @Range(min = 1, max = 5)
    private Integer rating;

    // FetchType.EAGER includes the Tour but this is slower than LAZY
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tour_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Tour tour;
    @PrePersist
    protected void onCreate() {
        date = LocalDateTime.now();
    }
}
