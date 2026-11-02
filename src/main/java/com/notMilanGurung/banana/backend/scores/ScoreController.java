package com.notMilanGurung.banana.backend.scores;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.notMilanGurung.banana.backend.auth.AuthPrincipal;
import com.notMilanGurung.banana.backend.exception.ErrorResponse;
import com.notMilanGurung.banana.backend.game.Difficulty;
import com.notMilanGurung.banana.backend.game.GameService;
import com.notMilanGurung.banana.backend.game.DTO.LeaderboardEntry;

@RestController
@RequestMapping("/scores")
public class ScoreController {

  private final GameService gameService;

  public ScoreController(GameService gameService) {
    this.gameService = gameService;
  }

  @GetMapping("/leaderboard")
  public List<LeaderboardEntry> leaderboard(@RequestParam(defaultValue = "NO_LIMIT") Difficulty difficulty) {
    return gameService.leaderboard(difficulty);
  }

  @GetMapping("/me")
  public Map<Difficulty, Integer> me(@AuthenticationPrincipal AuthPrincipal user) throws ErrorResponse {
    return gameService.myHighScores(user.username());
  }
}
