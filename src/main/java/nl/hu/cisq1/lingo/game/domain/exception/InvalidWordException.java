package nl.hu.cisq1.lingo.game.domain.exception;

public class InvalidWordException extends RuntimeException {
    public InvalidWordException(){
        super("Invalid word, doesn't have the right length of letters");
    }
}
