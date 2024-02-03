package ru.studiotg.minesweeper.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class GameInfoResponse {
    private UUID game_id;
    private int width;
    private int height;
    private int mines_count;
    private String[][] field;
    private boolean completed;
}
