package ru.studiotg.minesweeper.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewGameRequest {
    private int width;
    private int height;
    private int mines_count;
}
