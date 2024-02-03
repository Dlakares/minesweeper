package ru.studiotg.minesweeper.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper mapper;
    private final GameRepository gameRepository;
    private final GameMapper gameMapper;

    @PostConstruct
    public void init() {
        List<Game> games = gameRepository.findByEndedAtIsNull();
        for (GameDto dto : gameMapper.toListDto(games)) {
            put(dto);
        }
    }

    public Optional<Game> get(String id) {
        GameDto dto = (GameDto) redisTemplate.opsForValue().get(id);
        Game game = gameMapper.toEntity(dto);
        return Optional.ofNullable(game);
    }

    public boolean put(Game game) {
        GameDto dto = gameMapper.toDto(game);
        put(dto);
        return true;
    }

    private void put(GameDto dto) {
        try {
            String json = mapper.writeValueAsString(dto);
            redisTemplate.opsForValue().set(dto.getId().toString(), json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
