package nl.hu.cisq1.lingo.game.domain.exception;

public class LostGameException extends RuntimeException {
    public LostGameException(){
        super("You already lost this game");
    }
}
