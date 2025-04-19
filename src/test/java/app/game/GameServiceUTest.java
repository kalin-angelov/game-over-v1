package app.game;

import app.exceptions.InvalidInputException;
import app.game.model.Game;
import app.game.model.GamePlatform;
import app.game.repository.GameRepository;
import app.user.service.UserService;
import app.web.dto.GameRequest;
import app.web.dto.GameResponse;
import app.web.mapper.DtoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.GameService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
    void givenEmptyGameRequestDto_whenAddNewGam_thenExceptionIsThrow() {

        GameRequest dto = GameRequest.builder().build();
        assertThrows(InvalidInputException.class, () -> gameService.addNewGame(dto));
    }

    @Test
    void givenHappyPath_whenMappingGameToGameResponse() {

        Game game = Game.builder()
                .title("Ac 2")
                .description("Some description")
                .imgUrl("ImgUrl")
                .platform(GamePlatform.PC)
                .build();

        GameResponse resultDto = DtoMapper.toGameResponse(game);

        assertEquals(game, resultDto.getGame());
        assertEquals("Game successfully saved.", resultDto.getMessage());
        assertEquals(true, resultDto.isSuccessful());
    }

    @Test
    void givenExistingGamesInDatabase_whenGetAllGames_thenReturnThemAll() {

        List<Game> gameList = List.of(new Game(), new Game());

        when(gameRepository.findAll()).thenReturn(gameList);

        List<Game> games = gameService.getAllGames();

        assertThat(games).hasSize(2);
    }


}
