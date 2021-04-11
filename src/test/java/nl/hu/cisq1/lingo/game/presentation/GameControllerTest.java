package nl.hu.cisq1.lingo.game.presentation;

import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.game.data.SpringGameRepository;
import nl.hu.cisq1.lingo.game.domain.Game;
import nl.hu.cisq1.lingo.words.data.SpringWordRepository;
import nl.hu.cisq1.lingo.words.domain.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(CiTestConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GameControllerTest {
    @MockBean
    private SpringWordRepository wordRepository;
    @MockBean
    private SpringGameRepository gameRepository;
    @Autowired
    private MockMvc mockMvc;

    Game game;

    @BeforeEach
    void setup(){
        game = new Game();
    }

    @Test
    @DisplayName("New game made")
    void newGame() throws Exception {
        when(wordRepository.findRandomWordByLength(5)).thenReturn(Optional.of(new Word("tests")));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/game/game");
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

//    @Test
//    @DisplayName("Making new round")
//    void newRound() throws Exception {
//        when(wordRepository.findRandomWordByLength(5)).thenReturn(Optional.of(new Word("tests")));
//        when(gameRepository.findById(0L)).thenReturn(Optional.of(game));
//        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/game/game/0/round");
//        mockMvc.perform(requestBuilder)
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.state", is("PLAYING")))
//                .andExpect(jsonPath("$.score", is(0)))
//                .andExpect(jsonPath("$.hint", is("t....")))
//                .andExpect(jsonPath("$.round", is(1)));
//    }
//
//    @Test
//    @DisplayName("Making a guess")
//    void guess() throws InvalidWordLength, RoundAlreadyPlayingException, Exception {
//        game.newRound("tests");
//        when(gameRepository.findById(0L)).thenReturn(Optional.of(game));
//        AttemptDTO attemptDTO = new AttemptDTO("plats");
//        String guessBody = new ObjectMapper().writeValueAsString(attemptDTO);
//        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/game/game/0/guess").contentType(MediaType.APPLICATION_JSON).content(guessBody);
//        mockMvc.perform(requestBuilder)
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.state", is("PLAYING")))
//                .andExpect(jsonPath("$.score", is(25)))
//                .andExpect(jsonPath("$.hint", is("t..ts")));
//    }
//
//    @Test
//    @DisplayName("Making a guess when round hasn't started yet")
//    void guessRoundNotStarted() throws Exception {
//        when(gameRepository.findById(0L)).thenReturn(Optional.of(game));
//        AttemptDTO attemptDTO = new AttemptDTO("plats");
//        String guessBody = new ObjectMapper().writeValueAsString(attemptDTO);
//        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/game/game/0/guess").contentType(MediaType.APPLICATION_JSON).content(guessBody);
//        mockMvc.perform(requestBuilder).andExpect(jsonPath("$.errorCode").value("CONFLICT"));
//    }
//
//    @Test
//    @DisplayName("Starting round with no game active")
//    void noGame() throws Exception {
//        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/game/game/2/round");
//        mockMvc.perform(requestBuilder).andExpect(jsonPath("$.errorCode").value("NOT FOUND"));
//    }
}