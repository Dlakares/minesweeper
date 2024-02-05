package ru.studiotg.minesweeper.util;

import org.junit.jupiter.api.Test;
import ru.studiotg.minesweeper.entity.Field;

import static org.junit.jupiter.api.Assertions.*;

public class GameProcessorTest {

    GameProcessor processor = new GameProcessor('X', ' ', 'c', '0');

    @Test
    public void testMakeTurn() {
        Field field = new Field();
        field.setField(new String[]{"ccX", "ccc", "ccc"});
        boolean result = processor.makeTurn(0, 0, field);
        assertTrue(result);
        assertArrayEquals(new String[]{"01X", "011", "000"}, field.getField());
    }

    @Test
    public void testIsCompleted() {
        GameProcessor gameProcessor = new GameProcessor();
        Field field = new Field();
        field.setField(new String[]{"01X", "011", "000"});
        boolean result = gameProcessor.isCompleted(field);

        // Проверяем ожидаемый результат
        assertTrue(result);
    }

    @Test
    public void testClickOnOpenedCell_must_fail() {
        Field field = new Field();
        field.setField(new String[]{"01X", "011", "000"});
        assertThrows(IllegalArgumentException.class, () -> processor.makeTurn(0, 0, field));
    }

    @Test
    void openAll() {
        Field field = new Field();
        field.setField(new String[]{"ccX", "ccc", "ccc"});
        processor.openAll(field);

        assertArrayEquals(new String[]{"01X", "011", "000"}, field.getField());
    }
    // Добавьте другие тестовые кейсы по аналогии
}