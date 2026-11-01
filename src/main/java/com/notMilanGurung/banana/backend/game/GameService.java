package com.notMilanGurung.banana.backend.game;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.notMilanGurung.banana.backend.exception.ErrorResponse;
import com.notMilanGurung.banana.backend.game.DTO.AnswerRequest;
import com.notMilanGurung.banana.backend.game.DTO.AnswerResult;
import com.notMilanGurung.banana.backend.game.DTO.GameState;
import com.notMilanGurung.banana.backend.game.DTO.LeaderboardEntry;
import com.notMilanGurung.banana.backend.user.User;
import com.notMilanGurung.banana.backend.user.UserRepository;

@Service
public class GameService {

  private final GameRepository gameRepository;
  private final UserRepository userRepository;
  private final PuzzleClient puzzleClient;

  public GameService(GameRepository gameRepository, UserRepository userRepository,
                     PuzzleClient puzzleClient) {
    this.gameRepository = gameRepository;
    this.userRepository = userRepository;
    this.puzzleClient = puzzleClient;
  }

  @Transactional
  public GameState start(String username) throws ErrorResponse {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new ErrorResponse(HttpStatus.UNAUTHORIZED, "User not found"));

    PuzzleClient.Puzzle p = puzzleClient.fetch();

    Game game = new Game();
    game.setUser(user);
    game.setCurrentQuestion(p.question());
    game.setCurrentSolution(p.solution());
    gameRepository.save(game);

    return new GameState(game.getId(), game.getCurrentQuestion(), 0);
  }

  @Transactional
  public AnswerResult answer(UUID userId, AnswerRequest req) throws ErrorResponse {
    Game game = gameRepository.findByIdAndUser_id(req.gameId(), userId)
        .orElseThrow(() -> new ErrorResponse(HttpStatus.NOT_FOUND, "Game not found"));

    if (!game.isActive()) {
      throw new ErrorResponse(HttpStatus.BAD_REQUEST, "Game is already over");
    }

    User user = game.getUser();

    if (req.answer() == game.getCurrentSolution()) {
      // fetch first, so a failure here doesn't leave a half-updated game
      PuzzleClient.Puzzle next = puzzleClient.fetch();

      game.setStreak(game.getStreak() + 1);
      game.setCurrentQuestion(next.question());
      game.setCurrentSolution(next.solution());

      if (game.getStreak() > user.getHighScore()) {
        user.setHighScore(game.getStreak());
      }
      return new AnswerResult(true, false, game.getStreak(), user.getHighScore(), next.question());
    }

    game.setActive(false);
    return new AnswerResult(false, true, game.getStreak(), user.getHighScore(), null);
  }

  public List<LeaderboardEntry> leaderboard() {
    return userRepository.findTop10ByOrderByHighScoreDesc().stream()
        .map(u -> new LeaderboardEntry(u.getUsername(), u.getHighScore()))
        .toList();
  }

  public int myHighScore(String username) throws ErrorResponse {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new ErrorResponse(HttpStatus.UNAUTHORIZED, "User not found"))
        .getHighScore();
  }
}
