package ru.studiotg.minesweeper.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.studiotg.minesweeper.dto.GameInfoResponse;
import ru.studiotg.minesweeper.dto.GameTurnRequest;
import ru.studiotg.minesweeper.dto.NewGameRequest;
import ru.studiotg.minesweeper.service.GameService;

@RestController
@Slf4j
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    @PostMapping("/new")
    public ResponseEntity<GameInfoResponse> createGame(@RequestBody NewGameRequest request) {
        log.info("Endpoint <createGame>, uri '/game/v1/new' was called successfully");
        return ResponseEntity
                .ok(gameService.createNewGame(request));
    }

    @PostMapping("/turn")
    public ResponseEntity<GameInfoResponse> makeTurn(GameTurnRequest request) {
        log.info("Endpoint <makeTurn>, uri '/game/v1/turn' was called successfully");
        return ResponseEntity
                .ok(gameService.makeTurn(request));
    }
}
