package ru.studiotg.minesweeper.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class GameTurnRequest {
    private UUID game_id;
    private int col;
    private int row;
}
