package com.notMilanGurung.banana.backend.game.DTO;

import com.notMilanGurung.banana.backend.game.Difficulty;

import jakarta.validation.constraints.NotNull;

public record StartRequest(@NotNull Difficulty difficulty) {}
