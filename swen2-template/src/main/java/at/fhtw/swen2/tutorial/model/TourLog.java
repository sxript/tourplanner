package at.fhtw.swen2.tutorial.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Builder
@Getter
@Setter
public class TourLog {

    private Date date;
    private Long duration;
    private double distance;


}
