package com.notMilanGurung.banana.backend.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import com.notMilanGurung.banana.backend.exception.ErrorResponse;
import com.notMilanGurung.banana.backend.user.User;
import com.notMilanGurung.banana.backend.user.UserResponse;;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication API", description = "Endpoints for user registration, login, and JWT")
public class AuthController {

  private final AuthService authService;
  private final JwtService jwtService;
  private final CookieService cookieService;

  public AuthController(AuthService authService, JwtService jwtService, CookieService cookieService) {
    this.authService = authService;
    this.jwtService = jwtService;
    this.cookieService = cookieService;
  }

  @PostMapping("/register")
  @Operation(summary = "Register a new user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User successfully registered"),
      @ApiResponse(responseCode = "400", description = "Username taken")
  })
  public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request) throws ErrorResponse {
    return ResponseEntity.status(200).body(authService.register(request));
  }

  @PostMapping("/login")
  @Operation(summary = "Login an existing user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Login successful!"),
      @ApiResponse(responseCode = "400", description = "Authencication failed")
  })
  public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest request,
      HttpServletResponse res) throws ErrorResponse {

    User user = authService.login(request);
    cookieService.addAuthCookies(res,
        jwtService.generateAccessToken(user.getUsername()),
        jwtService.generateRefreshToken(user.getUsername()));
    return ResponseEntity.ok(UserResponse.from(user));
  }

  @PostMapping("/refresh")
  @Operation(summary = "Refresh the access token and refresh token")
  public ResponseEntity<Void> refresh(HttpServletRequest req, HttpServletResponse res) throws ErrorResponse {
    String refresh = cookieService.read(req, CookieService.REFRESH);
    if (refresh == null)
      throw new ErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid Refresh Token");
    try {
      Claims claims = jwtService.parse(refresh);
      if (!"refresh".equals(claims.get("type", String.class))) {
        throw new ErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid Refresh Token");
      }
      String username = claims.getSubject();
      cookieService.addAuthCookies(res,
          jwtService.generateAccessToken(username),
          jwtService.generateRefreshToken(username)); // rotates the refresh token
      return ResponseEntity.ok().build();
    } catch (JwtException e) {
      throw new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error refreshing token. Please try again later");
    }
  }

  @PostMapping("/logout")
  @Operation(summary = "Logout user")
  public ResponseEntity<Void> logout(HttpServletResponse res) {
    cookieService.clearAuthCookies(res);
    return ResponseEntity.ok().build();
  }
}
