package app.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginResponse {

    private String token;
    private boolean successful;
    private String message;
}
