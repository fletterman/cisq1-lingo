package nl.hu.cisq1.lingo.game.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.hu.cisq1.lingo.game.domain.exception.InvalidWordException;
import nl.hu.cisq1.lingo.game.domain.exception.NotPlayingException;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Round {
    @Id
    @GeneratedValue
    private Long id;
    private String wordToGuess;
    private int round;
    private GameState state;
    private String hint;
    @ElementCollection
    private List<String> attempts;
    @OneToMany
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Feedback> history;

    public Round(String wordToGuess){
        this.wordToGuess = wordToGuess;
        this.round = 0;
        this.state = GameState.PLAYING;
        this.history = new ArrayList<>();
        this.attempts = new ArrayList<>();
        setFirstHint();
    }

    public String guess(String guess) throws NotPlayingException, InvalidWordException {
        if (this.state != GameState.PLAYING){
            throw new NotPlayingException("This round is not ongoing");
        }
        Feedback feedback = new Feedback(this.wordToGuess, guess);
        this.history.add(feedback);
        String hint = giveHint();
        if (feedback.guessed()){
            this.state = GameState.WON;
        } else if (getAttemptsLength() == 5){
            this.state = GameState.ELIMINATED;
        } else {
            this.state = GameState.PLAYING;
        }
        return hint;
    }

    public String giveHint(){
        if (this.history.size() > 0){
            Feedback feedback = history.get(history.size() - 1);
            this.hint = feedback.getHint(this.hint);
        }

        this.attempts.add(this.hint);
        return this.hint;
    }

    public void setFirstHint(){
        String[] lettersToGuess = wordToGuess.split("");
        this.hint = lettersToGuess[0] + ".".repeat(lettersToGuess.length - 1);
    }

    public int getCurrentWordLength(){
        String[] lettersToGuess = wordToGuess.split("");
        return lettersToGuess.length;
    }

    public int getAttemptsLength(){
        return this.attempts.size();
    }
}
