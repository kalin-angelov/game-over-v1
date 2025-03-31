package app.web.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class ErrorResponse {

    private int status;
    private String message;
    private String time;

    public ErrorResponse (int status, String message) {
        this.status = status;
        this.message = message;
        this.time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a"));
    }
}
