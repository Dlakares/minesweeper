package ru.studiotg.minesweeper.util;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.studiotg.minesweeper.dto.NewGameRequest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class NewGameRequestValidatorTest {

    NewGameRequestValidator validator = new NewGameRequestValidator(30);

    @ParameterizedTest
    @MethodSource("invalidData")
    void check_must_fail(NewGameRequest request) {
        assertThrows(IllegalArgumentException.class,() -> validator.check(request));
    }

    @ParameterizedTest
    @MethodSource("validData")
    void check_must_pass(NewGameRequest request) {
        assertDoesNotThrow(() -> validator.check(request));
    }

    private static Stream<Arguments> invalidData() {
        return Stream.of(
                Arguments.of(NewGameRequest.builder()
                        .width(0)
                        .height(0)
                        .mines_count(10)
                        .build()),
                Arguments.of(NewGameRequest.builder()
                        .width(31)
                        .height(31)
                        .mines_count(10)
                        .build()),
                Arguments.of(NewGameRequest.builder()
                        .width(0)
                        .height(13)
                        .mines_count(10)
                        .build()),
                Arguments.of(NewGameRequest.builder()
                        .width(13)
                        .height(0)
                        .mines_count(10)
                        .build()),
                Arguments.of(NewGameRequest.builder()
                        .width(5)
                        .height(5)
                        .mines_count(25)
                        .build()),
                Arguments.of(NewGameRequest.builder()
                        .width(5)
                        .height(5)
                        .mines_count(26)
                        .build()),
                Arguments.of(NewGameRequest.builder()
                        .width(5)
                        .height(5)
                        .mines_count(0)
                        .build()),
                Arguments.of(NewGameRequest.builder()
                        .width(1)
                        .height(1)
                        .mines_count(1)
                        .build())
        );
    }

    private static Stream<Arguments> validData() {
        return Stream.of(
                Arguments.of(NewGameRequest.builder()
                        .width(1)
                        .height(2)
                        .mines_count(1)
                        .build()),
                Arguments.of(NewGameRequest.builder()
                        .width(20)
                        .height(14)
                        .mines_count(10)
                        .build()),
                Arguments.of(NewGameRequest.builder()
                        .width(30)
                        .height(30)
                        .mines_count(899)
                        .build()),
                Arguments.of(NewGameRequest.builder()
                        .width(1)
                        .height(2)
                        .mines_count(1)
                        .build())
        );
    }
}