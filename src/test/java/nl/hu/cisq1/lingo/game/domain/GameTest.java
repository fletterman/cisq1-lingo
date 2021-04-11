package nl.hu.cisq1.lingo.game.domain;

import nl.hu.cisq1.lingo.game.domain.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    Game game;

    @BeforeEach
    void newGame(){
        game = new Game();
    }

    @Test
    @DisplayName("Throw when word to guess is invalid")
    void invalidWord(){
        assertThrows(InvalidWordLength.class, () -> game.newRound("test"));
    }

    @Test
    @DisplayName("Valid word to guess")
    void validWord(){
        assertDoesNotThrow(() -> game.newRound("baard"));
    }

    @Test
    @DisplayName("Next word is longer when last word was guessed")
    void nextLength() throws InvalidWordLength, RoundAlreadyPlayingException, InvalidWordException, NotPlayingException, RoundsNotStartedException {
        game.newRound("baard");
        game.guess("baard");
        assertEquals(game.getWordLength(), 6);
    }

    @Test
    @DisplayName("Still playing")
    void stillPlaying() throws InvalidWordLength, RoundAlreadyPlayingException {
        game.newRound("baard");
        assertThrows(RoundAlreadyPlayingException.class, () -> game.newRound("baarde"));
    }

    @Test
    @DisplayName("Not able to guess when no round is ongoing")
    void noRound(){
        assertThrows(RoundsNotStartedException.class, () -> game.guess("testen"));
    }

    @ParameterizedTest
    @MethodSource("scores")
    @DisplayName("Score per attempt")
    void score(int attempts, int score) throws InvalidWordException, NotPlayingException, RoundsNotStartedException, InvalidWordLength, RoundAlreadyPlayingException {
        game.newRound("baard");
        for (int i = 0; i < attempts - 1; i++) {
            game.guess("paard");
        }
        game.guess("baard");
        assertEquals(score, game.getScore());
    }

    static Stream<Arguments> scores(){
        return Stream.of(
                Arguments.of(1, 25),
                Arguments.of(2, 20),
                Arguments.of(3, 15),
                Arguments.of(4, 10),
                Arguments.of(5, 5)
        );
    }
}
