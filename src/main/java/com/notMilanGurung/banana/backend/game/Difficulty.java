package com.notMilanGurung.banana.backend.game;

import lombok.Getter;

@Getter
public enum Difficulty {
  NO_LIMIT(0), TEN(10_000), FIVE(5_000), TWENTY(20_000);

  private final long limitMs;
  Difficulty(long limitMs) { this.limitMs = limitMs; }

  public boolean isTimed() { return limitMs > 0; }
}
