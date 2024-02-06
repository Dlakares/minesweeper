package ru.studiotg.minesweeper.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.studiotg.minesweeper.cache.GameCache;
import ru.studiotg.minesweeper.dto.GameInfoResponse;
import ru.studiotg.minesweeper.dto.GameTurnRequest;
import ru.studiotg.minesweeper.dto.NewGameRequest;
import ru.studiotg.minesweeper.entity.Field;
import ru.studiotg.minesweeper.entity.Game;
import ru.studiotg.minesweeper.mapper.GameInfoMapper;
import ru.studiotg.minesweeper.repository.FieldRepository;
import ru.studiotg.minesweeper.repository.GameRepository;
import ru.studiotg.minesweeper.util.FieldBuilder;
import ru.studiotg.minesweeper.util.GameProcessor;
import ru.studiotg.minesweeper.util.NewGameRequestValidator;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameService {
    private final FieldBuilder fieldBuilder;
    private final NewGameRequestValidator newGameRequestValidator;
    private final FieldRepository fieldRepository;
    private final GameRepository gameRepository;
    private final GameInfoMapper gameInfoMapper;
    private final GameCache cache;
    private final GameProcessor gameProcessor;

    public GameInfoResponse createNewGame(NewGameRequest request) {
        log.info("Starting to create new game {}x{} with {} mines", request.getWidth(), request.getHeight(), request.getMines_count());
        newGameRequestValidator.check(request);
        Field field = fieldBuilder.getGameField(request.getWidth(), request.getHeight(), request.getMines_count());
        Game game = new Game(field);
        game = gameRepository.save(game);
        fieldRepository.save(field);
        cache.put(game);
        log.info("Created new game {}x{} with {} mines. Game id: {}", request.getWidth(), request.getHeight(), request.getMines_count(), game.getId());
        return gameInfoMapper.toGameInfoResponse(game, false, false);
    }

    public GameInfoResponse makeTurn(GameTurnRequest request) {
        log.info("Making turn {}x{} in game with id {}", request.getCol(), request.getRow(), request.getGame_id());
        if(!cache.contains(request.getGame_id())) {
            log.info("Game with id {} not found", request.getGame_id());
            throw new IllegalArgumentException("Game is over or expired time to play");
        }
        Game game = cache.get(request.getGame_id()).get();
        boolean isTurnMine = !gameProcessor.makeTurn(request.getCol(), request.getRow(), game.getField());
        boolean isWin = gameProcessor.isCompleted(game.getField());
        if(isTurnMine || isWin) {
            log.info("Game with id {} is over", request.getGame_id());
            game.setResult(true);
            game.setEndedAt(LocalDateTime.now());
            gameRepository.save(game);
            cache.delete(request.getGame_id());
            gameProcessor.openAll(game.getField());
            return gameInfoMapper.toGameInfoResponse(game, true, isWin);
        }
        log.info("Game with id {}, make turn row: {} col: {}", request.getGame_id(), request.getRow(), request.getCol());
        cache.put(game);
        return gameInfoMapper.toGameInfoResponse(game, false, false);
    }
}
