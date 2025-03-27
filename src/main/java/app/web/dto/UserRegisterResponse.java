package app.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegisterResponse {

    private String email;
    private boolean successful;
    private String message;
}
