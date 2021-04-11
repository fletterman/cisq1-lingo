package nl.hu.cisq1.lingo.game.domain;

import nl.hu.cisq1.lingo.game.domain.exception.InvalidWordException;
import nl.hu.cisq1.lingo.game.domain.exception.NotPlayingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RoundTest {
    Round round;

    @BeforeEach
    void setup(){
        round = new Round("tests");
    }
    @Test
    @DisplayName("First hint when the game has just started")
    void firstHint(){
        assertEquals(round.giveHint(), "t....");
    }

    @Test
    @DisplayName("New hint after guess")
    void newHint() throws NotPlayingException, InvalidWordException {
        assertEquals(round.guess("plets"),"t..ts");
    }

    @Test
    @DisplayName("Round won")
    void won() throws NotPlayingException, InvalidWordException {
        round.guess("tests");
        assertEquals(round.getState(), GameState.WON);
    }

    @Test
    @DisplayName("Still playing")
    void playing() throws NotPlayingException, InvalidWordException {
        round.guess("pests");
        assertEquals(round.getState(), GameState.PLAYING);
    }

    @Test
    @DisplayName("Lost the round")
    void lost() throws NotPlayingException, InvalidWordException {
        for (int i = 0; i < 5; i++) {
            round.guess("pests");
        }
        assertEquals(round.getState(), GameState.ELIMINATED);
    }

    @Test
    @DisplayName("Can't guess when game has ended")
    void ended() throws NotPlayingException, InvalidWordException {
        for (int i = 0; i < 5; i++) {
            round.guess("pests");
        }
        assertThrows(NotPlayingException.class, () -> round.guess("pests"));
    }
}
