package com.notMilanGurung.banana.backend.game.DTO;

public record AnswerResult(boolean correct, boolean gameOver, int streak,
                           int highScore, String nextQuestion) {}
