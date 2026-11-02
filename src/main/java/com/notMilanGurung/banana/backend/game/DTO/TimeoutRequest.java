package com.notMilanGurung.banana.backend.game.DTO;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record TimeoutRequest(@NotNull UUID gameId) {}
