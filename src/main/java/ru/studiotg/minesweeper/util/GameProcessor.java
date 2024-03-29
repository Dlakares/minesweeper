package ru.studiotg.minesweeper.util;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.studiotg.minesweeper.entity.Field;

import java.util.Arrays;

@Component
@Slf4j
@RequiredArgsConstructor
@AllArgsConstructor
public class GameProcessor {
    private final int[][] MOVES = new int[][]{
            {1, 1},
            {1, 0},
            {1, -1},
            {0, -1},
            {-1, -1},
            {-1, 0},
            {-1, 1},
            {0, 1}
    };

    @Value("${minesweeper.mine-char}")
    private char MINE;
    @Value("${minesweeper.empty-char}")
    private char EMPTY;
    @Value("${minesweeper.closed}")
    private char CLOSED;
    @Value("${minesweeper.open}")
    private char OPEN;

    public boolean makeTurn(int col, int row, Field field) {
        log.info("Make turn: col = {}, row = {}, field with id = {}", col, row, field.getId());
        char[][] fieldArray = Arrays.stream(field.getField()).map(String::toCharArray).toArray(char[][]::new);
        if (fieldArray[row][col] == MINE) {
            log.info("Game lost. Field with id = {}", field.getId());
            return false;
        }
        if (fieldArray[row][col] == OPEN || fieldArray[row][col] != CLOSED) {
            log.info("Field with id = {} already opened", field.getId());
            throw new IllegalArgumentException("Cell is already opened");
        }
        log.info("Starting to open field with id = {}", field.getId());
        openCell(row, col, fieldArray);
        log.info("Field with id = {} opened", field.getId());
        field.setField(Arrays.stream(fieldArray).map(String::new).toArray(String[]::new));
        return true;
    }

    private void openCell(int row, int col, char[][] fieldArray) {
        int minesCount = getNearestMinesCount(row, col, fieldArray);
        fieldArray[row][col] = minesCount == 0 ? OPEN : Character.forDigit(minesCount, 10);
        if (minesCount == 0) {
            for (int[] move : MOVES) {
                int nextRow = row + move[0];
                int nextCol = col + move[1];
                if (isValid(nextRow, nextCol, fieldArray)) {
                    openCell(nextRow, nextCol, fieldArray);
                }
            }
        }
    }

    private int getNearestMinesCount(int row, int col, char[][] fieldArray) {
        int count = 0;
        for (int[] move : MOVES) {
            int nextRow = row + move[0];
            int nextCol = col + move[1];
            if (isValid(nextRow, nextCol, fieldArray) && isMine(nextRow, nextCol, fieldArray)) {
                count++;
            }
        }
        return count;
    }

    public boolean isCompleted(Field field) {
        int count = 0;
        for (String row : field.getField()) {
            for (char c : row.toCharArray()) {
                if (c == MINE || c == CLOSED) {
                    count++;
                }
            }
        }
        return count == field.getMinesCount();
    }

    private boolean isValid(int row, int col, char[][] fieldArray) {
        return row >= 0 &&
                row < fieldArray.length &&
                col >= 0 &&
                col < fieldArray[row].length &&
                fieldArray[row][col] != OPEN;
    }

    private boolean isMine(int row, int col, char[][] fieldArray) {
        return fieldArray[row][col] == MINE;
    }

    public void openAll(Field field) {
        log.info("Open all field with id = {}", field.getId());
        String[] gameField = field.getField();
        char[][] gameFieldArray = Arrays.stream(gameField).map(String::toCharArray).toArray(char[][]::new);
        for (int row = 0; row < gameField.length; row++) {
            for (int col = 0; col < gameField[row].length(); col++) {
                if (gameField[row].charAt(col) == CLOSED) {
                    gameFieldArray[row][col] = getNearestMinesCount(row, col, gameFieldArray) == 0 ? OPEN : Character.forDigit(getNearestMinesCount(row, col, gameFieldArray), 10);
                }
            }
            gameField[row] = new String(gameFieldArray[row]);
        }
        log.info("Field with id = {} opened", field.getId());
    }
}
