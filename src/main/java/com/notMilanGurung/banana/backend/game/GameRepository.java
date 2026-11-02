package com.notMilanGurung.banana.backend.game;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.notMilanGurung.banana.backend.user.User;

public interface GameRepository extends JpaRepository<Game, UUID> {
  Optional<Game> findByIdAndUser_id(UUID gameId, UUID userId);

  @Modifying
  @Query("update Game g set g.active = false where g.user = :user and g.active = true")
  void deactivateAll(@Param("user") User user);
}
