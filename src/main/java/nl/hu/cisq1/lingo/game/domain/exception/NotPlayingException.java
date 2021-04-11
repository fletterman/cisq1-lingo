package nl.hu.cisq1.lingo.game.domain.exception;

public class NotPlayingException extends RuntimeException {
    public NotPlayingException(String string) {
        super(string);
    }
}
