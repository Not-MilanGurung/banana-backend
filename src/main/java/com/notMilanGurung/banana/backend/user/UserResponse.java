package com.notMilanGurung.banana.backend.user;

import java.util.UUID;

public record UserResponse(UUID id, String username, String email) {
  public static UserResponse from(User u) {
    return new UserResponse(u.getId(), u.getUsername(), u.getEmail());
  }
}
