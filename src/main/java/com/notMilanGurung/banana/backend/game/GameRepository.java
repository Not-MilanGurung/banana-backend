package com.notMilanGurung.banana.backend.game;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, UUID> {
  List<Game> findByUserIdOrderByScoreDesc(UUID userId);
}
