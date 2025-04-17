package app.game;

import app.exceptions.InvalidInputException;
import app.exceptions.NotFoundException;
import app.game.model.Game;
import app.game.model.GamePlatform;
import app.game.repository.GameRepository;
import app.game.service.GameService;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.service.UserService;
import app.web.dto.GameRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceUTest {

    @Mock
    private GameRepository gameRepository;
    @Mock
    private UserService userService;

    @InjectMocks
    private GameService gameService;

    @Test
    void shouldThrowException_whenGivenEmptyGameRequestDTO() {

        GameRequest dto = GameRequest.builder().build();
        assertThrows(InvalidInputException.class, () -> gameService.addNewGame(dto));
    }

    @Test
    void shouldSaveGame_whenGivenCorrectGameRequestDTO() {

        UUID gameId = UUID.randomUUID();
        User user = new User();
        GameRequest dto = GameRequest.builder()
                .title("AC 2")
                .imgUrl("https://upload.wikimedia.org/wikipedia/en/7/77/Assassins_Creed_2_Box_Art.JPG")
                .description("Some description")
                .platform(GamePlatform.PC)
                .build();
        Game game = Game.builder()
                .id(gameId)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .imgUrl(dto.getImgUrl())
                .platform(dto.getPlatform())
                .createdOn(LocalDateTime.now())
                .owner(user)
                .build();

        gameService.addNewGame(dto);

        verify(gameRepository).save(game);
    }
}
