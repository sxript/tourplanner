package at.fhtw.swen2.tutorial.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class Tour {
    private String name;
}
