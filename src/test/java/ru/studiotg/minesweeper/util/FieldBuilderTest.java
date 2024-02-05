package ru.studiotg.minesweeper.util;

import org.junit.jupiter.api.Test;
import ru.studiotg.minesweeper.entity.Field;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FieldBuilderTest {

    FieldBuilder builder = new FieldBuilder(' ','X', 'c');

    @Test
    void getGameField() {
        Field field = builder.getGameField(3, 3, 1);
        assertAll(() -> {
            assertEquals(3, field.getWidth());
            assertEquals(3, field.getHeight());
            assertEquals(1, field.getMinesCount());
        });
    }
}