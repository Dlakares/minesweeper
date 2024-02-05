package ru.studiotg.minesweeper.util;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.studiotg.minesweeper.dto.NewGameRequest;

@Component
@RequiredArgsConstructor
@AllArgsConstructor
public class NewGameRequestValidator {

    @Value("${minesweeper.max-size:30}")
    private int maxSize;

    public void check(NewGameRequest request) {
        StringBuilder exceptions = new StringBuilder();
        int width = request.getWidth();
        int height = request.getHeight();
        int minesCount = request.getMines_count();
        if(width > maxSize) {
            exceptions.append("Width must be less than ").append(maxSize).append(". \n");
        }
        if(width < 1) {
            exceptions.append("Width must be greater than 0. \n");
        }
        if(height < 1) {
            exceptions.append("Height must be greater than 0. \n");
        }
        if(height == 1 && width == 1) {
            exceptions.append("Height and width must be greater than 1. \n");
        }
        if(height > maxSize) {
            exceptions.append("Height must be less than ").append(maxSize).append(". \n");
        }
        if(minesCount >= width * height) {
            exceptions.append("Mines count must be less than ").append(width * height).append(". \n");
        }
        if(minesCount < 1) {
            exceptions.append("Mines count must be greater than 0. \n");
        }

        if(!exceptions.isEmpty()) {
            throw new IllegalArgumentException(exceptions.toString());
        }
    }
}
