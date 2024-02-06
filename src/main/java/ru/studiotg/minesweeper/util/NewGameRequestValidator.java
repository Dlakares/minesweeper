package ru.studiotg.minesweeper.util;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.studiotg.minesweeper.dto.NewGameRequest;

@Component
@RequiredArgsConstructor
@Slf4j
@AllArgsConstructor
public class NewGameRequestValidator {

    @Value("${minesweeper.max-size:30}")
    private int maxSize;

    public void check(NewGameRequest request) {
        log.info("Check new game request: {}", request);
        StringBuilder exceptions = new StringBuilder();
        int width = request.getWidth();
        int height = request.getHeight();
        int minesCount = request.getMines_count();
        if(width > maxSize) {
            exceptions.append("Width must be less than ").append(maxSize).append(". \n");
            log.info("Width {} is greater than max size {} ", width, maxSize);
        }
        if(width < 1) {
            exceptions.append("Width must be greater than 0. \n");
            log.info("Width {} is less than 1 ", width);
        }
        if(height < 1) {
            exceptions.append("Height must be greater than 0. \n");
            log.info("Height {} is less than 1 ", height);
        }
        if(height == 1 && width == 1) {
            exceptions.append("Height and width must be greater than 1. \n");
            log.info("Height {} and width {} are equal to 1 ", height, width);
        }
        if(height > maxSize) {
            exceptions.append("Height must be less than ").append(maxSize).append(". \n");
            log.info("Height {} is greater than max size {} ", height, maxSize);
        }
        if(minesCount >= width * height) {
            exceptions.append("Mines count must be less than ").append(width * height).append(". \n");
            log.info("Mines count {} is greater than size {} ", minesCount, width * height);
        }
        if(minesCount < 1) {
            exceptions.append("Mines count must be greater than 0. \n");
            log.info("Mines count {} is less than 1 ", minesCount);
        }

        if(!exceptions.isEmpty()) {
            log.info("New game request is invalid: {}", exceptions.toString());
            throw new IllegalArgumentException(exceptions.toString());
        } else {
            log.info("New game request is valid");
        }
    }
}
