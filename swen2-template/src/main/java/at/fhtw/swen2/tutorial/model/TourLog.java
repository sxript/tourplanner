package at.fhtw.swen2.tutorial.model;

import lombok.*;

@Getter
@Setter
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TourLog {
    private Long id;
    private String date;
    private String comment;
    private Integer totalTime;
    private String difficulty;
    private Integer rating;
}
