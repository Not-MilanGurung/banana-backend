package com.notMilanGurung.banana.backend.game;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notMilanGurung.banana.backend.auth.AuthPrincipal;
import com.notMilanGurung.banana.backend.exception.ErrorResponse;
import com.notMilanGurung.banana.backend.game.DTO.AnswerRequest;
import com.notMilanGurung.banana.backend.game.DTO.AnswerResult;
import com.notMilanGurung.banana.backend.game.DTO.GameState;
import com.notMilanGurung.banana.backend.game.DTO.StartRequest;
import com.notMilanGurung.banana.backend.game.DTO.TimeoutRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/game")
public class GameController {

  private final GameService gameService;

  public GameController(GameService gameService) {
    this.gameService = gameService;
  }

  @PostMapping("/start")
  public ResponseEntity<GameState> start(@Valid @RequestBody StartRequest req, @AuthenticationPrincipal  AuthPrincipal user)
      throws ErrorResponse {
    return ResponseEntity.ok(gameService.start(user.id(), req.difficulty()));
  }

  @PostMapping("/timeout")
  public ResponseEntity<AnswerResult> timeout(@Valid @RequestBody TimeoutRequest req, @AuthenticationPrincipal AuthPrincipal user)
      throws ErrorResponse {
    return ResponseEntity.ok(gameService.timeout(user.id(), req.gameId()));
  }

  @PostMapping("/answer")
  public ResponseEntity<AnswerResult> answer(@Valid @RequestBody AnswerRequest req,
                                             @AuthenticationPrincipal AuthPrincipal user) throws ErrorResponse {
    return ResponseEntity.ok(gameService.answer(user.id(), req));
  }
}
