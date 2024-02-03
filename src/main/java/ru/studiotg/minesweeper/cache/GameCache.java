package ru.studiotg.minesweeper.cache;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import ru.studiotg.minesweeper.dto.GameDto;
import ru.studiotg.minesweeper.entity.Game;
import ru.studiotg.minesweeper.mapper.GameMapper;
import ru.studiotg.minesweeper.repository.GameRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class GameCache {
    private final RedisTemplate<String, Object> redisTemplate;
    private final GameRepository gameRepository;
    private final GameMapper gameMapper;

    @PostConstruct
    public void init() {
        List<Game> games = gameRepository.findByEndedAtIsNull();
        for (GameDto dto : gameMapper.toListDto(games)) {
            redisTemplate.opsForValue().set(dto.getId().toString(), dto);
        }
    }

    public Optional<Game> get(String id) {
        GameDto dto = (GameDto) redisTemplate.opsForValue().get(id);
        Game game = gameMapper.toEntity(dto);
        return Optional.ofNullable(game);
    }
}
