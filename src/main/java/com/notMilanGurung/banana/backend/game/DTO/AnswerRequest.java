package com.notMilanGurung.banana.backend.game.DTO;

import java.util.UUID;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AnswerRequest(
    @NotNull UUID gameId,
    @NotNull @Min(0) @Max(9) Integer answer
) {}
