package nl.hu.cisq1.lingo.game.presentation;

import nl.hu.cisq1.lingo.game.application.GameService;
import nl.hu.cisq1.lingo.game.domain.Progress;
import nl.hu.cisq1.lingo.game.domain.exception.*;
import nl.hu.cisq1.lingo.game.presentation.dto.AttemptDTO;
import nl.hu.cisq1.lingo.game.presentation.dto.NewGameDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;

    public GameController(GameService service){
        this.gameService = service;
    }

    @PostMapping("/game")
    public NewGameDTO newGame(){
        NewGameDTO newGameDTO = new NewGameDTO(gameService.newGame());
        if (newGameDTO == null){
            throw new GameNotFoundException();
        }
        return newGameDTO;
    }

    @PostMapping("/game/{id}/round")
    public Progress newRound(@PathVariable Long id) throws LostGameException, GameNotFoundException, RoundAlreadyPlayingException, InvalidWordLengthException {
        return gameService.newRound(id);
    }

    @PostMapping("game/{id}/guess")
    public Progress guess(@PathVariable Long id, @RequestBody AttemptDTO attemptDTO) throws GameNotFoundException, LostGameException, RoundsNotStartedException, NotPlayingException, InvalidWordException {
        return gameService.guess(id, attemptDTO.getAttempt());
    }
}
