package ru.studiotg.minesweeper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.studiotg.minesweeper.entity.Field;

@Repository
public interface FieldRepository extends JpaRepository<Field, Long> {
}
