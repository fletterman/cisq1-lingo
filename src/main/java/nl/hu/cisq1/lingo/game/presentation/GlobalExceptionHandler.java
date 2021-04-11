package nl.hu.cisq1.lingo.game.presentation;

import nl.hu.cisq1.lingo.game.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<ExceptionResponse> gameNotFound(GameNotFoundException ex){
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setErrorCode("NOT FOUND");
        exceptionResponse.setErrorMessage(ex.getMessage());
        exceptionResponse.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LostGameException.class)
    public ResponseEntity<ExceptionResponse> lostGame(LostGameException ex){
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setErrorCode("CONFLICT");
        exceptionResponse.setErrorMessage(ex.getMessage());
        exceptionResponse.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotPlayingException.class)
    public ResponseEntity<ExceptionResponse> notPlaying(NotPlayingException ex){
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setErrorCode("CONFLICT");
        exceptionResponse.setErrorMessage(ex.getMessage());
        exceptionResponse.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RoundAlreadyPlayingException.class)
    public ResponseEntity<ExceptionResponse> alreadyPlaying(RoundAlreadyPlayingException ex){
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setErrorCode("CONFLICT");
        exceptionResponse.setErrorMessage(ex.getMessage());
        exceptionResponse.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RoundsNotStartedException.class)
    public ResponseEntity<ExceptionResponse> roundNotStarted(RoundsNotStartedException ex){
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setErrorCode("CONFLICT");
        exceptionResponse.setErrorMessage(ex.getMessage());
        exceptionResponse.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.CONFLICT);
    }
}
