package app.web.dto;

import app.game.model.GamePlatform;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GameRequest {

    private String title;
    private String description;
    private String imgUrl;
    private GamePlatform platform;
}
