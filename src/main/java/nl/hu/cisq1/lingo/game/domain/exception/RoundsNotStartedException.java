package nl.hu.cisq1.lingo.game.domain.exception;

public class RoundsNotStartedException extends Throwable {
    public RoundsNotStartedException(){
        super("The round hasn't started yet");
    }
}
