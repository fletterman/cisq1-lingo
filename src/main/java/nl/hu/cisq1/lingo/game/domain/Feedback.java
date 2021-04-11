package nl.hu.cisq1.lingo.game.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nl.hu.cisq1.lingo.game.domain.exception.InvalidWordException;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@EqualsAndHashCode
@Entity
@NoArgsConstructor
public class Feedback {
    @Id
    @GeneratedValue
    private Long id;
    @ElementCollection
    private List<Evaluation> evaluation;
    private String attempt;
    private String wordToGuess;

    public Feedback(String wordToGuess, String attempt) throws InvalidWordException {
        this.wordToGuess = wordToGuess;
        this.attempt = attempt;
        this.evaluation = makeGuess(wordToGuess, attempt);
    }

    private List<Evaluation> makeGuess(String wordToGuess, String attempt) throws InvalidWordException {
        String[] attemptLetters = attempt.split("");
        String[] wordToGuessLetters = wordToGuess.split("");
        List<Evaluation> evaluationList = new ArrayList<>();

        if (wordToGuessLetters.length != attemptLetters.length){
            invalidWord();
        }

        List<String> presentLetters = new ArrayList<>();
        for (int i = 0; i < wordToGuessLetters.length; i++) {
            if (wordToGuessLetters[i].equals(attemptLetters[i])){
                evaluationList.add(Evaluation.CORRECT);
            } else {
                presentLetters.add(wordToGuessLetters[i]);
                evaluationList.add(null);
            }
        }

        for (int i = 0; i < wordToGuessLetters.length; i++) {
            if (evaluationList.get(i) != Evaluation.CORRECT){
                if (presentLetters.contains(attemptLetters[i])){
                    evaluationList.set(i, Evaluation.PRESENT);
                    presentLetters.remove(attemptLetters[i]);
                } else {
                    evaluationList.set(i, Evaluation.ABSENT);
                }
            }
        }
        return evaluationList;
    }

    public boolean guessed(){
        return this.evaluation.stream().allMatch(Evaluation.CORRECT::equals);
    }

    public void invalidWord() throws InvalidWordException {
        throw new InvalidWordException();
    }

    public String getHint(String hint){
        String word = "";
        String[] letters = this.wordToGuess.split("");
        if (hint == null){
            hint = letters[0] + ".".repeat(letters.length - 1);
        }

        String[] hintLetters = hint.split("");
        for (int i = 0; i < letters.length; i++) {
            if (this.evaluation.get(i).equals(Evaluation.CORRECT)){
                word += letters[i];
            } else {
                word += hintLetters[i];
            }
        }
        return word;
    }
}
