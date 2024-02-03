package ru.studiotg.minesweeper.mapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.studiotg.minesweeper.dto.GameInfoResponse;
import ru.studiotg.minesweeper.entity.Field;
import ru.studiotg.minesweeper.entity.Game;

import java.util.UUID;

@Component
public class GameInfoMapper {

    @Value("${minesweeper.empty-char}")
    private char EMPTY;
    @Value("${minesweeper.mine-char}")
    private char MINE;
    @Value("${minesweeper.closed}")
    private char CLOSED;

    public GameInfoResponse toGameInfoResponse(Game game) {
        if(game == null) {
            return null;
        }
        Field field = game.getField();

        UUID uuid = game.getId();
        int width = field.getWidth();
        int height = field.getHeight();
        int minesCount = field.getMinesCount();
        boolean completed = game.isResult();
        String[][] gameField = getField(field.getField());

        return GameInfoResponse.builder()
                .game_id(uuid)
                .width(width)
                .height(height)
                .mines_count(minesCount)
                .completed(completed)
                .field(gameField)
                .build();
    }

    private String[][] getField(String[] field) {
        String[][] res = new String[field.length][field[0].length()];
        for(int i = 0; i < field.length; i++) {
            for(int j = 0; j < field[i].length(); j++) {
                if(field[i].charAt(j) == (CLOSED) || field[i].charAt(j) == (MINE)) {
                    res[i][j] = Character.toString(EMPTY);
                }
            }
        }

        return res;
    }
}
