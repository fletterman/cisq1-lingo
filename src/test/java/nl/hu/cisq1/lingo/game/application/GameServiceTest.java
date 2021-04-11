package nl.hu.cisq1.lingo.game.application;

import nl.hu.cisq1.lingo.game.data.SpringGameRepository;
import nl.hu.cisq1.lingo.game.domain.Game;
import nl.hu.cisq1.lingo.game.domain.exception.*;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
public class GameServiceTest {
    WordService wordService;
    SpringGameRepository gameRepository;
    GameService gameService;
    Game game;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void before(){
        game = new Game();
        wordService = mock(WordService.class);
        when(wordService.provideRandomWord(5)).thenReturn("baard");
        when(wordService.provideRandomWord(6)).thenReturn("baarde");
        when(wordService.provideRandomWord(7)).thenReturn("baarden");
        gameRepository = mock(SpringGameRepository.class);
        when(gameRepository.findById(0L)).thenReturn(java.util.Optional.of(game));
        gameService = new GameService(wordService, gameRepository);
    }

    @Test
    @DisplayName("Wasn't able to find the game")
    void gameNotFound(){
        assertThrows(GameNotFoundException.class, () -> gameService.newRound(2L));
        assertThrows(GameNotFoundException.class, () -> gameService.guess(2L, " "));
    }

    @Test
    @DisplayName("The game hasn't been started yet")
    void notStarted(){
        assertThrows(RoundsNotStartedException.class, () -> gameService.guess(0L," "));
    }

    @Test
    @DisplayName("The game has already been started")
    void alreadyStarted() throws LostGameException, GameNotFoundException, RoundAlreadyPlayingException, InvalidWordLengthException {
        gameService.newRound(0L);
        assertThrows(RoundAlreadyPlayingException.class, () -> gameService.newRound(0L));
    }

    @Test
    @DisplayName("The game has already ended")
    void alreadyEnded() throws LostGameException, GameNotFoundException, RoundAlreadyPlayingException, InvalidWordLengthException, RoundsNotStartedException, NotPlayingException, InvalidWordException {
        gameService.newRound(0L);
        for (int i = 0; i < 5; i++) {
            gameService.guess(0L, "guess");
        }
        assertThrows(LostGameException.class, () -> gameService.guess(0L, "test"));
    }

    @Test
    @DisplayName("New round when already eliminated")
    void alreadyEliminated() throws Exception {
        gameService.newRound(0L);
        for (int i = 0; i < 5; i++) {
            gameService.guess(0L, "guess");
        }
        assertThrows(LostGameException.class, () -> gameService.newRound(0L));
    }
}
