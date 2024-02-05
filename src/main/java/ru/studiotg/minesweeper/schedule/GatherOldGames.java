package ru.studiotg.minesweeper.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.studiotg.minesweeper.entity.Game;
import ru.studiotg.minesweeper.repository.GameRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class GatherOldGames {
    private final GameRepository gameRepository;
    @Value("${spring.data.redis.ttl}")
    private Duration ttl;

    @Async
    @Scheduled(fixedRateString = "${spring.data.redis.ttl}")
    public void gatherOldGames() {
        log.info("Starting gather old games older than {} minutes", ttl.toMinutes());

        List<Game> oldGames = gameRepository.findByEndedAtIsNullAndStartedAtBefore(LocalDateTime.now().minus(ttl));
        oldGames.forEach(game -> game.setEndedAt(LocalDateTime.now()));
        gameRepository.saveAll(oldGames);

        log.info("Finished gather old games older than {} minutes", ttl.toMinutes());
    }
}
