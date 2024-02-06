package ru.studiotg.minesweeper;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import ru.studiotg.minesweeper.cache.GameCache;
import ru.studiotg.minesweeper.dto.GameTurnRequest;
import ru.studiotg.minesweeper.dto.NewGameRequest;
import ru.studiotg.minesweeper.entity.Field;
import ru.studiotg.minesweeper.entity.Game;
import ru.studiotg.minesweeper.repository.GameRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class MinesweeperApplicationTests {

    @LocalServerPort
    private Integer port;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14.5-alpine");

    static {
        GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:5.0.3-alpine:")).withExposedPorts(6379);
        redis.start();
        System.setProperty("spring.data.redis.host", redis.getHost());
        System.setProperty("spring.data.redis.port", String.valueOf(redis.getMappedPort(6379)));
    }

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    GameRepository gameRepository;
    @Autowired
    GameCache cache;

    @Test
    void shouldCreateGame() {
        List<Game> games = List.of(
                Game.builder().id(UUID.randomUUID()).field(Field.builder().field(new String[]{"XcX", "cXc", "Xcc"}).build()).result(false).startedAt(LocalDateTime.now()).build()
        );
        gameRepository.saveAll(games);

        given()
                .contentType(ContentType.JSON)
                .body(NewGameRequest.builder().width(10).height(10).mines_count(1).build())
                .when()
                .post("http://localhost:" + port + "/new")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldMakeTurn() {
        Game game = Game.builder()
                .field(Field.builder().field(new String[]{"ccX", "ccc", "ccc"}).build())
                .result(false)
                .startedAt(LocalDateTime.now())
                .build();
        gameRepository.save(game);
        UUID id = game.getId();
        cache.put(game);

        given()
                .contentType(ContentType.JSON)
                .body(GameTurnRequest.builder().row(0).col(0).game_id(id).build())
                .when()
                .post("http://localhost:" + port + "/turn")
                .then()
                .statusCode(200);
    }
}
