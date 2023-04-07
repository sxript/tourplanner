package at.fhtw.swen2.tutorial.util;

import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.model.TourLog;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DataExportDTO {
    private Tour tour;
    private List<TourLog> tourLogs;
}
