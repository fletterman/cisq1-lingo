package nl.hu.cisq1.lingo.game.domain.exception;

public class RoundAlreadyPlayingException extends RuntimeException {
    public RoundAlreadyPlayingException(){
        super("The round is already ongoing");
    }
}
