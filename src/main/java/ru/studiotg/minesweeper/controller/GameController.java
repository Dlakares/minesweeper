package ru.studiotg.minesweeper.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Create new game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game created successfully"),
            @ApiResponse(responseCode = "404", description = "Game is already played or time for play expired")
    })
    @PostMapping("/new")
    public ResponseEntity<GameInfoResponse> createGame(@Parameter(required = true, description = "New game request") @RequestBody NewGameRequest request) {
        log.info("Endpoint <createGame>, uri '/game/v1/new' was called successfully");
        return ResponseEntity
                .ok(gameService.createNewGame(request));
    }

    @Operation(summary = "Make turn")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Turn was made successfully"),
            @ApiResponse(responseCode = "404", description = "Game is already played or time for play expired"),
            @ApiResponse(responseCode = "400", description = "Try to make turn in finished game or open is opened cell")
    })
    @PostMapping("/turn")
    public ResponseEntity<GameInfoResponse> makeTurn(@Parameter(required = true, description = "Turn request") @RequestBody GameTurnRequest request) {
        log.info("Endpoint <makeTurn>, uri '/game/v1/turn' was called successfully");
        return ResponseEntity
                .ok(gameService.makeTurn(request));
    }
}
