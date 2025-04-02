package app.web;

import app.game.model.Game;
import app.game.service.GameService;
import app.security.AuthenticationMetadata;
import app.user.service.UserService;
import app.web.dto.GameRequest;
import app.web.dto.GameResponse;
import app.web.mapper.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static app.web.Paths.API_V1_BASE_PATH;

@RestController
@RequestMapping(API_V1_BASE_PATH + "/games")
public class GameController {

    private final GameService gameService;
    private final UserService userService;

    @Autowired
    public GameController(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    @GetMapping
    public List<Game> fetchAllGames () {
        return gameService.getAllGames();
    }

    @GetMapping("/{id}")
    public Game fetchGame(@PathVariable UUID id) {
        return gameService.getGame(id);
    }

    @PostMapping("/new")
    public ResponseEntity<GameResponse> createNewGame(@RequestBody GameRequest gameRequest, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

//        User user = userService.findUserById(authenticationMetadata.getId());
        Game game = gameService.addNewGame(gameRequest);
        GameResponse gameResponse = DtoMapper.toGameResponse(game);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(gameResponse);
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<GameResponse> editGame (@RequestBody GameRequest gameRequest, @PathVariable UUID id) {

        Game game = gameService.editGame(gameRequest, id);
        GameResponse gameResponse = DtoMapper.toGameResponse(game);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gameResponse);
    }

}
