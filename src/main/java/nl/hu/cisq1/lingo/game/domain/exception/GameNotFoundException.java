package nl.hu.cisq1.lingo.game.domain.exception;

public class GameNotFoundException extends Throwable {
    public GameNotFoundException(){
        super("Couldn't find the game");
    }
}
