package at.technikum.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private Date timestamp;
    private Integer statusCode;
    private String message;
    private String description;
}
