package ru.studiotg.minesweeper.dto;

import lombok.Data;

@Data
public class FieldDto {

    private int width;
    private int height;
    private int minesCount;
    private String[] field;
}
