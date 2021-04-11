package nl.hu.cisq1.lingo.game.application;

import lombok.AllArgsConstructor;
import nl.hu.cisq1.lingo.game.data.SpringGameRepository;
import nl.hu.cisq1.lingo.game.domain.Game;
import nl.hu.cisq1.lingo.game.domain.GameState;
import nl.hu.cisq1.lingo.game.domain.Progress;
import nl.hu.cisq1.lingo.game.domain.exception.*;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class GameService {
    private final WordService wordService;
    private final SpringGameRepository gameRepository;

    public Long newGame(){
        Game game = new Game();
        this.gameRepository.save(game);
        return game.getId();
    }

    public Progress newRound(Long id) throws RoundAlreadyPlayingException, InvalidWordLengthException, GameNotFoundException, LostGameException {
        Game game = this.gameRepository.findById(id).orElseThrow(() -> new GameNotFoundException());

        if (game.getCurrentRound() != null){
            if (game.getState().equals(GameState.ELIMINATED)){
                throw new LostGameException();
            }
            if (game.getState().equals(GameState.PLAYING)){
                throw new RoundAlreadyPlayingException();
            }
        }
        String wordToGuess = this.wordService.provideRandomWord(game.getWordLength());
        Progress progress = game.newRound(wordToGuess);
        this.gameRepository.save(game);
        return progress;
    }

    public Progress guess(Long id, String attempt) throws RoundsNotStartedException, LostGameException, GameNotFoundException, NotPlayingException, InvalidWordException {
        Game game = this.gameRepository.findById(id).orElseThrow(() -> new GameNotFoundException());
        if (game.getCurrentRound() != null){
            if (game.getState().equals(GameState.ELIMINATED)){
                throw new LostGameException();
            }
        }
        Progress progress = game.guess(attempt);
        this.gameRepository.save(game);
        return progress;
    }
}
