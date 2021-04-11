package nl.hu.cisq1.lingo.game.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Progress {
    private Long gameId;
    private int score;
    private int round;
    private GameState state;
    private List<Feedback> feedback;
    private String hint;
}
