package ru.studiotg.minesweeper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.studiotg.minesweeper.dto.GameInfoResponse;
import ru.studiotg.minesweeper.dto.GameTurnRequest;
import ru.studiotg.minesweeper.dto.NewGameRequest;

@Service
@RequiredArgsConstructor
public class GameService {

    public GameInfoResponse createNewGame(NewGameRequest request) {
        return null;
    }

    public GameInfoResponse makeTurn(GameTurnRequest request) {
        return null;
    }
}
