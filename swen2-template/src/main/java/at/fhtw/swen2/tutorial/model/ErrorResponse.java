package at.fhtw.swen2.tutorial.model;


import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Slf4j
public class ErrorResponse {
    private Date timestamp;
    private Integer statusCode;
    private String message;
    private String description;
    private List<String> errors;
}
