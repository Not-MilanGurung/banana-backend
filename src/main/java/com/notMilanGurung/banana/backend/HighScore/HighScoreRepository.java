package com.notMilanGurung.banana.backend.HighScore;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.notMilanGurung.banana.backend.game.Difficulty;
import com.notMilanGurung.banana.backend.user.User;

public interface HighScoreRepository extends JpaRepository<HighScore, Long> {
  Optional<HighScore> findByUserAndDifficulty(User user, Difficulty difficulty);
  List<HighScore> findByUser(User user);
  List<HighScore> findTop10ByDifficultyOrderByScoreDesc(Difficulty difficulty);
}