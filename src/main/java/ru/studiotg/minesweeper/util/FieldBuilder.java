package ru.studiotg.minesweeper.util;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.studiotg.minesweeper.entity.Field;

import java.util.Arrays;
import java.util.Random;

@Component
@AllArgsConstructor
@Slf4j
@RequiredArgsConstructor
public class FieldBuilder {
    @Value("${minesweeper.empty-char}")
    private char EMPTY;
    @Value("${minesweeper.mine-char}")
    private char MINE;
    @Value("${minesweeper.closed}")
    private char CLOSED;


    public Field getGameField(int width, int height, int minesCount) {
        log.info("Creating field for new game {}x{} with {} mines", width, height, minesCount);
        String[] field = buildField(width, height, minesCount);
        log.info("Successfully field for new game {}x{} with {} mines", width, height, minesCount);
        return new Field(width, height, minesCount, field);
    }

    private String[] buildField(int width, int height, int minesCount) {
        String[] field = new String[width];
        for (int i = 0; i < width; i++) {
            char[] row = new char[height];
            Arrays.fill(row, CLOSED);
            field[i] = new String(row);
        }
        int remainingMines = minesCount;
        while (remainingMines != 0) {
            int row = getRandomInt(0, width - 1);
            int col = getRandomInt(0, height - 1);
            if (field[row].charAt(col) != MINE) {
                field[row] = field[row].substring(0, col) + MINE + field[row].substring(col + 1);
                remainingMines--;
            }
        }

        return field;
    }

    private int getRandomInt(int min, int max) {
        Random rand = new Random();

        return rand.nextInt(max - min + 1) + min;
    }
}
