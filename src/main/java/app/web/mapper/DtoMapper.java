package app.web.mapper;

import app.game.model.Game;
import app.user.model.User;
import app.web.dto.GameResponse;
import app.web.dto.UserLoginResponse;
import app.web.dto.UserRegisterResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

    public static UserRegisterResponse toRegisterResponse(User user) {

        return UserRegisterResponse.builder()
                .email(user.getEmail())
                .successful(true)
                .message("User successfully register.")
                .build();
    }

    public static UserLoginResponse toLoginResponse(String token) {

        return UserLoginResponse.builder()
                .token(token)
                .successful(true)
                .message("Welcome")
                .build();
    }

    public static GameResponse toGameResponse(Game game) {

        return GameResponse.builder()
                .successful(true)
                .message("Game successfully created.")
                .game(game)
                .build();
    }
}
