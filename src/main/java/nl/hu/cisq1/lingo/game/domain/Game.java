package nl.hu.cisq1.lingo.game.domain;

import nl.hu.cisq1.lingo.game.domain.exception.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Game implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private int score;
    @OneToMany
    @Cascade(CascadeType.ALL)
    private final List<Round> rounds;
    private int wordLength;
    private String hint;

    public Game(){
        this.score = 0;
        this.wordLength = 5;
        this.rounds = new ArrayList<>();
    }

    public Progress newRound(String wordToGuess) throws RoundAlreadyPlayingException, InvalidWordLengthException {
        if (wordToGuess.length() != wordLength){
            throw new InvalidWordLengthException();
        }
        if (getTotalRounds() >= 1){
            if (rounds.get(rounds.size() - 1).getState() == GameState.PLAYING){
                throw new RoundAlreadyPlayingException();
            }
        }
        Round round = new Round(wordToGuess);
        this.rounds.add(round);
        getNextWordLength();
        this.hint = round.getHint();
        return showProgress();
    }

    public Progress guess(String attempt) throws RoundsNotStartedException, NotPlayingException, InvalidWordException {
        if (getTotalRounds() == 0){
            throw new RoundsNotStartedException();
        }
        Round round = rounds.get(rounds.size() - 1);
        calculateScore();
        hint = round.guess(attempt);
        return showProgress();
    }

    public void getNextWordLength(){
        int wordToGuessLength = rounds.get(rounds.size() - 1).getCurrentWordLength();
        if (wordToGuessLength == 5){
            this.wordLength = 6;
        } else if (wordToGuessLength == 6){
            this.wordLength = 7;
        } else {
            this.wordLength = 5;
        }
    }

    public void calculateScore(){
        Round round = rounds.get(rounds.size() - 1);
        this.score = 5 * (5 - round.getAttemptsLength() - 1) + 5;
    }

    public int getTotalRounds(){
        return rounds.size();
    }

    public Round getCurrentRound(){
        if (this.rounds.size() == 0){
            return null;
        } else {
            return rounds.get(rounds.size() - 1);
        }
    }

    public GameState getState(){
        return getCurrentRound().getState();
    }

    public Progress showProgress(){
        return new Progress(id, this.score, getTotalRounds(), getState(), getCurrentRound().getHistory(), hint);
    }

    public Long getId(){
        return id;
    }

    public int getWordLength(){
        return wordLength;
    }

    public int getScore(){
        return score;
    }
}
