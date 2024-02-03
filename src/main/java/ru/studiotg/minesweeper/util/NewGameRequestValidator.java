package ru.studiotg.minesweeper.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.studiotg.minesweeper.dto.NewGameRequest;

@Component
@RequiredArgsConstructor
public class NewGameRequestValidator {

    @Value("${minesweeper.max-size}")
    private int maxSize;

    public void check(NewGameRequest request) {
        StringBuilder exceptions = new StringBuilder();
        int width = request.getWidth();
        int height = request.getHeight();
        int minesCount = request.getMines_count();
        if(width > maxSize) {
            exceptions.append("Width must be less than ").append(maxSize).append(". \n");
        }
        if(height > maxSize) {
            exceptions.append("Height must be less than ").append(maxSize).append(". \n");
        }
        if(minesCount >= width * height) {
            exceptions.append("Mines count must be less than ").append(width * height).append(". \n");
        }

        if(!exceptions.isEmpty()) {
            throw new RuntimeException(exceptions.toString());
        }
    }
}
