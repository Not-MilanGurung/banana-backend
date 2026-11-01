package com.notMilanGurung.banana.backend.game.DTO;

import java.util.UUID;

public record GameState(UUID gameId, String question, int streak) {}
