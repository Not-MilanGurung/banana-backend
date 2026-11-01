package com.notMilanGurung.banana.backend.user;

import java.security.AuthProvider;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notMilanGurung.banana.backend.auth.AuthPrincipal;

@RestController
@RequestMapping("/users")
public class UserController {

  @GetMapping("/me")
  public ResponseEntity<AuthPrincipal> me(@AuthenticationPrincipal AuthPrincipal user) {
    return ResponseEntity.ok(user);
  }
}
