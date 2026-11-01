package com.notMilanGurung.banana.backend.game;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, UUID> {
  Optional<Game> findByIdAndUser_id(UUID gameId, UUID userId);
}
