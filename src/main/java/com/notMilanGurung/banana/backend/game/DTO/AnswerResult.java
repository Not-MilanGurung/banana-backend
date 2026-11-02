package com.notMilanGurung.banana.backend.game.DTO;

public record AnswerResult(boolean correct, boolean gameOver, String reason, // "WRONG" | "TIMEOUT" | null
                           int streak, int highScore, String nextQuestion) {}
