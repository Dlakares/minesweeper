package ru.studiotg.minesweeper.dto;

import lombok.Data;

@Data
public class FieldDto {

    private int width;
    private int height;
    private int mineCount;
    private String[] field;
}
