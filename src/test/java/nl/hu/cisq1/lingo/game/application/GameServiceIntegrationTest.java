package nl.hu.cisq1.lingo.game.application;

import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.game.domain.GameState;
import nl.hu.cisq1.lingo.game.domain.Progress;
import nl.hu.cisq1.lingo.game.domain.exception.*;
import nl.hu.cisq1.lingo.words.data.SpringWordRepository;
import nl.hu.cisq1.lingo.words.domain.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import(CiTestConfiguration.class)
@Transactional
public class GameServiceIntegrationTest {
    @Autowired
    private GameService gameService;
    @MockBean
    private SpringWordRepository wordRepository;
    private Long id;

    @BeforeEach
    void setup() throws LostGameException, GameNotFoundException, RoundAlreadyPlayingException, InvalidWordLength {
        when(wordRepository.findRandomWordByLength(5)).thenReturn(Optional.of(new Word("baard")));
        id = gameService.newGame();
        gameService.newRound(id);
    }

    @Test
    @DisplayName("Starting new round")
    void newRound() throws LostGameException, GameNotFoundException, RoundAlreadyPlayingException, InvalidWordLength {
        Long id = gameService.newGame();
        Progress progress = gameService.newRound(id);
        assertEquals(GameState.PLAYING, progress.getState());
        assertEquals(0, progress.getScore());
        assertEquals(5, progress.getHint().length());
    }

    @Test
    @DisplayName("Round was won")
    void roundWon() throws NotPlayingException, GameNotFoundException, InvalidWordException, RoundsNotStartedException, LostGameException {
        Progress progress = gameService.guess(id, "baard");
        assertEquals(GameState.WON, progress.getState());
        assertEquals(25, progress.getScore());
    }

    @Test
    @DisplayName("Lost round")
    void roundLost() throws NotPlayingException, GameNotFoundException, InvalidWordException, RoundsNotStartedException, LostGameException {
        Progress progress = null;
        for (int i = 0; i < 5; i++) {
            progress = gameService.guess(id, "tests");
        }
        assertEquals(GameState.ELIMINATED, progress.getState());
        assertEquals(5, progress.getScore());
    }

    @Test
    @DisplayName("Playing in lost game")
    void lostGame() throws NotPlayingException, GameNotFoundException, InvalidWordException, RoundsNotStartedException, LostGameException {
        for (int i = 0; i < 5; i++) {
            gameService.guess(id, "tests");
        }
        assertThrows(LostGameException.class, () -> gameService.guess(id, "tests"));
    }
}
