package service;

import app.exceptions.InvalidInputException;
import app.exceptions.NotFoundException;
import app.game.model.Game;
import app.game.repository.GameRepository;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.GameRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final UserService userService;

    @Autowired
    public GameService(GameRepository gameRepository, UserService userService) {
        this.gameRepository = gameRepository;
        this.userService = userService;
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public Game getGame (UUID gameId) {
        return gameRepository.findById(gameId).orElseThrow(() -> new NotFoundException("Game with id [%s] do not exist.".formatted(gameId)));
    }

    public List<Game> getAllUserGames(UUID id) {
        User user = userService.findUserById(id);

        return  user.getGames();
    }

    public Game addNewGame(GameRequest gameRequest) {

        if (isGameDtoEmpty(gameRequest)) {
            throw new InvalidInputException("All fields are required.");
        }

        Game game = gameRepository.save(initializeGame(gameRequest));

        return game;
    }

    public Game editGame(GameRequest gameRequest, UUID gameId) {

        if (isGameDtoEmpty(gameRequest)) {
            throw new InvalidInputException("All fields are required.");
        }

        Game game = getGame(gameId);

        game.setTitle(gameRequest.getTitle());
        game.setDescription(gameRequest.getDescription());
        game.setImgUrl(gameRequest.getImgUrl());
        game.setPlatform(gameRequest.getPlatform());

        gameRepository.save(game);
        return game;
    }

    private Game initializeGame(GameRequest gameRequest) {
        return Game.builder()
                .title(gameRequest.getTitle())
                .description(gameRequest.getDescription())
                .imgUrl(gameRequest.getImgUrl())
                .platform(gameRequest.getPlatform())
                .createdOn(LocalDateTime.now())
                .build();
    }

    private boolean isGameDtoEmpty (GameRequest gameRequest) {
        return gameRequest == null ||
                (gameRequest.getTitle() == null ||
                gameRequest.getDescription() == null ||
                gameRequest.getImgUrl() == null ||
                gameRequest.getPlatform() == null);
    }
}
