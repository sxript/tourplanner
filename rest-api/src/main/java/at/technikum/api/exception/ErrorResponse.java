package at.technikum.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private Date timestamp;
    private Integer statusCode;
    private String message;
    private String description;
    private List<String> errors;
}
