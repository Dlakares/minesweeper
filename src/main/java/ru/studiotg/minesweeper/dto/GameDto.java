package ru.studiotg.minesweeper.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class GameDto {
    private UUID id;
    private FieldDto field;
    private boolean result;
    private LocalDateTime startedAt;
}
