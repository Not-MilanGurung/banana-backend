package com.notMilanGurung.banana.backend.auth;

import java.util.UUID;

import com.notMilanGurung.banana.backend.user.User;

public record AuthPrincipal(UUID id, String username) {
  public static AuthPrincipal from(User u) {
    return new AuthPrincipal(u.getId(), u.getUsername());
  }
} 
