package at.technikum.api.model;

import at.technikum.api.enums.Difficulty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tour_logs")
public class TourLog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tour_logs_generator")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date date;

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
        date = new Date();
    }
}