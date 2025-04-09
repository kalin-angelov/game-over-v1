package app.web.dto;

import app.game.model.Game;
import app.user.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class UserInfoResponse {

    private UUID id;
    private String username;
    private String email;
    private String imgUrl;
    private UserRole role;
    private boolean isActive;
    private List<Game> games;
}
