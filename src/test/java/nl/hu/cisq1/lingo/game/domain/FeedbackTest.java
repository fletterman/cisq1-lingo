package nl.hu.cisq1.lingo.game.domain;

import nl.hu.cisq1.lingo.game.application.GameService;
import nl.hu.cisq1.lingo.game.data.SpringGameRepository;
import nl.hu.cisq1.lingo.game.domain.exception.*;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static nl.hu.cisq1.lingo.game.domain.Evaluation.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FeedbackTest {
    WordService wordService;
    GameService gameService;
    SpringGameRepository gameRepository;

    @BeforeEach
    void before(){
        Game game = new Game();
        wordService = mock(WordService.class);
        when(wordService.provideRandomWord(5)).thenReturn("baard");
        when(wordService.provideRandomWord(6)).thenReturn("baarde");
        when(wordService.provideRandomWord(7)).thenReturn("baarden");
        gameRepository = mock(SpringGameRepository.class);
        when(gameRepository.findById(0L)).thenReturn(java.util.Optional.of(game));
        gameService = new GameService(wordService, gameRepository);
    }

    @ParameterizedTest
    @MethodSource("feedbackEvaluation")
    void testEvaluations(String wordToGuess, String attempt, List<Evaluation> evaluationList) throws InvalidWordException {
        Feedback feedback = new Feedback(wordToGuess, attempt);
        assertEquals(evaluationList, feedback.getEvaluation());
    }

    static Stream<Arguments> feedbackEvaluation(){
        return Stream.of(
                Arguments.of("BAARD", "BOGEN", List.of(CORRECT, ABSENT, ABSENT, ABSENT, ABSENT)),
                Arguments.of("BAARD", "BALEN", List.of(CORRECT, CORRECT, ABSENT, ABSENT, ABSENT)),
                Arguments.of("BAARD", "BRAAD", List.of(CORRECT, PRESENT, CORRECT, PRESENT, CORRECT)),
                Arguments.of("BAARD", "TESTS", List.of(ABSENT, ABSENT, ABSENT, ABSENT, ABSENT)),
                Arguments.of("TESTEN", "ABSENT", List.of(ABSENT, ABSENT, CORRECT, PRESENT, PRESENT, PRESENT)),
                Arguments.of("TESTEN", "TESTEN", List.of(CORRECT, CORRECT, CORRECT, CORRECT, CORRECT, CORRECT))
        );
    }

    @Test
    @DisplayName("Won when all letters are correct")
    void guessed() throws InvalidWordException {
        Feedback feedback = new Feedback("testen", "testen");
        assertTrue(feedback.guessed());
    }

    @Test
    @DisplayName("Not won if not all letters are correct")
    void notGuessed() throws InvalidWordException {
        Feedback feedback = new Feedback("testen", "tasten");
        assertFalse(feedback.guessed());
    }

    @Test
    @DisplayName("Invalid word if amount of letters isn't the same")
    void invalidWord() throws LostGameException, GameNotFoundException, RoundAlreadyPlayingException, InvalidWordLengthException {
        gameService.newRound(0L);
        assertThrows(InvalidWordException.class, () -> gameService.guess(0L, "test"));
    }

    @Test
    @DisplayName("New feedback when new guess")
    void newFeedback() throws InvalidWordException {
        Feedback feedback = new Feedback("testen", "testen");
        Feedback feedback1 = new Feedback("testen", "pesten");
        assertNotEquals(feedback, feedback1);
    }

    @Test
    @DisplayName("Same feedback with same guess")
    void sameFeedback() throws InvalidWordException {
        Feedback feedback = new Feedback("testen", "testen");
        Feedback feedback1 = new Feedback("testen", "testen");
        assertEquals(feedback, feedback1);
    }

    @ParameterizedTest
    @DisplayName("Hints given with each guess")
    @MethodSource("hints")
    void hint(String wordToGuess, String guess, String oldHint, String hint) throws InvalidWordException {
        Feedback feedback = new Feedback(wordToGuess, guess);
        assertEquals(hint, feedback.getHint(oldHint));
    }

    private static Stream<Arguments> hints(){
        return Stream.of(
                Arguments.of("TESTEN", "SCHOOL", null, "T....."),
                Arguments.of("TESTEN", "VASTEN", "...T..", "..STEN"),
                Arguments.of("TESTEN", "PAGINA", "..STEN", "..STEN"),
                Arguments.of("TESTEN", "TESTEN", "..STEN", "TESTEN")
        );
    }
}
