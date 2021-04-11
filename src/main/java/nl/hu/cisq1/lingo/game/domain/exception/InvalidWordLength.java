package nl.hu.cisq1.lingo.game.domain.exception;

public class InvalidWordLength extends Throwable {
    public InvalidWordLength(){
        super("Invalid length of letters");
    }
}
