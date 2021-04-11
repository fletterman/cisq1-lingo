package nl.hu.cisq1.lingo.game.domain.exception;

public class InvalidWordLengthException extends RuntimeException {
    public InvalidWordLengthException(){
        super("Invalid length of letters");
    }
}
