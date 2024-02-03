package ru.studiotg.minesweeper.dto;

import lombok.Data;

@Data
public class NewGameRequest {
    private int weight;
    private int height;
    private int minesCount;
}
