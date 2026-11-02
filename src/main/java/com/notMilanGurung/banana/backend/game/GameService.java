package com.notMilanGurung.banana.backend.game;

import java.time.Duration;
import java.time.Instant;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.notMilanGurung.banana.backend.HighScore.HighScore;
import com.notMilanGurung.banana.backend.HighScore.HighScoreRepository;
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
  private final HighScoreRepository highScoreRepository;
  private static final long GRACE_MS = 1500; // covers latency + image load

  public GameService(GameRepository gameRepository, UserRepository userRepository,
      PuzzleClient puzzleClient, HighScoreRepository highScoreRepository) {
    this.gameRepository = gameRepository;
    this.userRepository = userRepository;
    this.puzzleClient = puzzleClient;
    this.highScoreRepository = highScoreRepository;
  }


  @Transactional
  public GameState start(UUID userId, Difficulty difficulty) throws ErrorResponse {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ErrorResponse(HttpStatus.UNAUTHORIZED, "User not found"));

    PuzzleClient.Puzzle p = puzzleClient.fetch(); // fetch first
    gameRepository.deactivateAll(user); // one live game per user

    Game game = new Game();
    game.setUser(user);
    game.setDifficulty(difficulty);
    game.setCurrentQuestion(p.question());
    game.setCurrentSolution(p.solution());
    game.setPuzzleIssuedAt(Instant.now()); // clock starts after the fetch
    gameRepository.save(game);

    return new GameState(game.getId(), p.question(), 0, difficulty, difficulty.getLimitMs());
  }

  @Transactional
  public AnswerResult answer(UUID userId, AnswerRequest req) throws ErrorResponse {
    Game game = loadActive(userId, req.gameId());
    HighScore best = bestFor(game);

    if (isLate(game, GRACE_MS)) {
      game.setActive(false);
      return new AnswerResult(false, true, "TIMEOUT", game.getStreak(), best.getScore(), null);
    }

    if (req.answer() != game.getCurrentSolution()) {
      game.setActive(false);
      return new AnswerResult(false, true, "WRONG", game.getStreak(), best.getScore(), null);
    }

    PuzzleClient.Puzzle next = puzzleClient.fetch(); // before mutating anything
    game.setStreak(game.getStreak() + 1);
    game.setCurrentQuestion(next.question());
    game.setCurrentSolution(next.solution());
    game.setPuzzleIssuedAt(Instant.now());

    if (game.getStreak() > best.getScore())
      best.setScore(game.getStreak());
    return new AnswerResult(true, false, null, game.getStreak(), best.getScore(), next.question());
  }

  /** Called by the client when its countdown reaches zero. */
  @Transactional
  public AnswerResult timeout(UUID userId, UUID gameId) throws ErrorResponse {
    Game game = loadActive(userId, gameId);
    if (!isLate(game, 0)) {
      throw new ErrorResponse(HttpStatus.BAD_REQUEST, "Time has not run out");
    }
    game.setActive(false);
    return new AnswerResult(false, true, "TIMEOUT", game.getStreak(),
        bestFor(game).getScore(), null);
  }

  private Game loadActive(UUID userId, UUID gameId) throws ErrorResponse {
    Game game = gameRepository.findByIdAndUser_id(gameId, userId)
        .orElseThrow(() -> new ErrorResponse(HttpStatus.NOT_FOUND, "Game not found"));
    if (!game.isActive())
      throw new ErrorResponse(HttpStatus.BAD_REQUEST, "Game is already over");
    return game;
  }

  private boolean isLate(Game game, long graceMs) {
    Difficulty d = game.getDifficulty();
    if (!d.isTimed())
      return false;
    long elapsed = Duration.between(game.getPuzzleIssuedAt(), Instant.now()).toMillis();
    return elapsed > d.getLimitMs() + graceMs;
  }

  private HighScore bestFor(Game game) {
    return highScoreRepository.findByUserAndDifficulty(game.getUser(), game.getDifficulty())
        .orElseGet(() -> {
          HighScore h = new HighScore();
          h.setUser(game.getUser());
          h.setDifficulty(game.getDifficulty());
          return highScoreRepository.save(h);
        });
  }

  @Transactional(readOnly = true)
  public List<LeaderboardEntry> leaderboard(Difficulty difficulty) {
    return highScoreRepository.findTop10ByDifficultyOrderByScoreDesc(difficulty).stream()
        .filter(h -> h.getScore() > 0)
        .map(h -> new LeaderboardEntry(h.getUser().getUsername(), h.getScore()))
        .toList();
  }

  @Transactional(readOnly = true)
  public Map<Difficulty, Integer> myHighScores(String username) throws ErrorResponse {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new ErrorResponse(HttpStatus.UNAUTHORIZED, "User not found"));
    Map<Difficulty, Integer> out = new EnumMap<>(Difficulty.class);
    for (Difficulty d : Difficulty.values())
      out.put(d, 0);
    highScoreRepository.findByUser(user).forEach(h -> out.put(h.getDifficulty(), h.getScore()));
    return out;
  }
}
