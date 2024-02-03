package ru.studiotg.minesweeper.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.studiotg.minesweeper.dto.FieldDto;
import ru.studiotg.minesweeper.entity.Field;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FieldMapper {

    FieldDto toDto(Field field);
}
