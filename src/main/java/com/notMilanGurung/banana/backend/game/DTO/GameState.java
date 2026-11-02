package com.notMilanGurung.banana.backend.game.DTO;

import java.util.UUID;

import com.notMilanGurung.banana.backend.game.Difficulty;

public record GameState(UUID gameId, String question, int streak,
                        Difficulty difficulty, long timeLimitMs) {}
