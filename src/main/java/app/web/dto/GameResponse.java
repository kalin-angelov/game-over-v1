package app.web.dto;

import app.game.model.Game;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameResponse {

    private boolean successful;
    private String message;
    private Game game;
}
