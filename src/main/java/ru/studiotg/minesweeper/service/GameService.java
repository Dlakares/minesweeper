package ru.studiotg.minesweeper.service;

import lombok.RequiredArgsConstructor;
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
import ru.studiotg.minesweeper.util.NewGameRequestValidator;

@Service
@RequiredArgsConstructor
public class GameService {
    private final FieldBuilder fieldBuilder;
    private final NewGameRequestValidator newGameRequestValidator;
    private final FieldRepository fieldRepository;
    private final GameRepository gameRepository;
    private final GameInfoMapper gameInfoMapper;
    private final GameCache cache;

    public GameInfoResponse createNewGame(NewGameRequest request) {
        newGameRequestValidator.check(request);
        Field field = fieldBuilder.getGameField(request.getWidth(), request.getHeight(), request.getMines_count());
        Game game = new Game(field);
        game = gameRepository.saveAndFlush(game);
        fieldRepository.saveAndFlush(field);
        cache.put(game);

        return gameInfoMapper.toGameInfoResponse(game);
    }

    public GameInfoResponse makeTurn(GameTurnRequest request) {
        return null;
    }
}
