package ru.studiotg.minesweeper.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class GameInfoResponse {
    private UUID game_id;
    private int weight;
    private int height;
    private int minesCount;
    private boolean completed;
    private String field;
}
