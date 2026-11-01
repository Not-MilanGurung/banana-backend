package com.notMilanGurung.banana.backend.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
  
  class MeResponse {
    private  final String username;
    public MeResponse(String username) {
      this.username = username;
    }

    public String getUsername() { return this.username; }
  }
  
  @GetMapping("/me")
  public ResponseEntity<MeResponse> me(Authentication auth) { return ResponseEntity.ok(new MeResponse(auth.getName())); }
}
