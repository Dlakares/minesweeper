package ru.studiotg.minesweeper.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
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

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {
    @Mock
    private FieldBuilder fieldBuilder;
    @Mock
    private NewGameRequestValidator newGameRequestValidator;
    @Spy
    private FieldRepository fieldRepository;
    @Mock
    private GameRepository gameRepository;
    @Spy
    private GameInfoMapper gameInfoMapper;
    @Mock
    private GameCache cache;
    @Mock
    private GameProcessor gameProcessor;
    @InjectMocks
    GameService gameService;


    @Test
    void createNewGame() {
        Field field = Field.builder().field(new String[]{"c, c, X", "c, c, c", "c, c, c"}).build();
        when(fieldBuilder.getGameField(anyInt(), anyInt(), anyInt())).thenReturn(field);
        Game game = new Game(field);
        when(gameRepository.save(any(Game.class))).thenReturn(game);
        when(fieldRepository.save(any(Field.class))).thenReturn(field);
        GameInfoResponse response = gameService.createNewGame(NewGameRequest.builder().width(3).height(3).mines_count(1).build());
        verify(cache, times(1)).put(game);
        assertEquals(gameInfoMapper.toGameInfoResponse(game, false, false), response);
    }

    @Test
    void makeTurn() {
        UUID id = UUID.randomUUID();
        when(cache.contains(id)).thenReturn(true);
        Game game = new Game(Field.builder().field(new String[]{"c, c, X", "c, c, c", "c, c, c"}).build());
        when(cache.get(id)).thenReturn(Optional.of(game));
        when(gameProcessor.makeTurn(anyInt(), anyInt(), any(Field.class))).thenReturn(true);
        when(gameProcessor.isCompleted(any(Field.class))).thenReturn(false);
        gameService.makeTurn(GameTurnRequest.builder().game_id(id).col(0).row(0).build());
        verify(cache, times(1)).put(game);
        GameInfoResponse response = gameService.makeTurn(GameTurnRequest.builder().game_id(id).col(0).row(0).build());
        assertEquals(gameInfoMapper.toGameInfoResponse(game, true, false), response);
    }

    @Test
    void makeTurnException() {
        UUID id = UUID.randomUUID();
        when(cache.contains(id)).thenReturn(false);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            gameService.makeTurn(GameTurnRequest.builder().game_id(id).col(0).row(0).build());
        });
        assertEquals("Game is over or expired time to play", exception.getMessage());
    }

    @Test
    void makeTurnException_isMine() {
        UUID id = UUID.randomUUID();
        when(cache.contains(id)).thenReturn(true);
        Game game = new Game(Field.builder().field(new String[]{"c, c, X", "c, c, c", "c, c, c"}).build());
        game.setId(id);
        when(cache.get(id)).thenReturn(Optional.of(game));
        when(gameProcessor.makeTurn(anyInt(), anyInt(), any(Field.class))).thenReturn(false);
        when(gameProcessor.isCompleted(any(Field.class))).thenReturn(false);
        gameService.makeTurn(GameTurnRequest.builder().game_id(id).col(0).row(0).build());
        verify(cache, times(1)).delete(game.getId());
        verify(gameProcessor, times(1)).openAll(any(Field.class));
        assertEquals(gameInfoMapper.toGameInfoResponse(game, false, false), gameService.makeTurn(GameTurnRequest.builder().game_id(id).col(0).row(0).build()));
    }

    @Test
    void makeTurnException_isWin() {
        UUID id = UUID.randomUUID();
        when(cache.contains(id)).thenReturn(true);
        Game game = new Game(Field.builder().field(new String[]{"c, c, X", "c, c, c", "c, c, c"}).build());
        game.setId(id);
        when(cache.get(id)).thenReturn(Optional.of(game));
        when(gameProcessor.makeTurn(anyInt(), anyInt(), any(Field.class))).thenReturn(false);
        when(gameProcessor.isCompleted(any(Field.class))).thenReturn(true);
        gameService.makeTurn(GameTurnRequest.builder().game_id(id).col(0).row(0).build());
        verify(cache, times(1)).delete(game.getId());
        verify(gameProcessor, times(1)).openAll(any(Field.class));
        assertEquals(gameInfoMapper.toGameInfoResponse(game, false, true), gameService.makeTurn(GameTurnRequest.builder().game_id(id).col(0).row(0).build()));
    }
}