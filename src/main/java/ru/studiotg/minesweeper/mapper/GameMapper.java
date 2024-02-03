package ru.studiotg.minesweeper.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.studiotg.minesweeper.dto.GameDto;
import ru.studiotg.minesweeper.entity.Game;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GameMapper {

    @Mapping(target = "fieldId", source = "field.id")
    GameDto toDto(Game game);

    List<GameDto> toListDto(List<Game> games);

    @Mapping(source = "fieldId", target = "field", ignore = true)
    Game toEntity(GameDto dto);
}
