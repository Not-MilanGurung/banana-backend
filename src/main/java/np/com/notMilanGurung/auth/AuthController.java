package np.com.notMilanGurung.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import np.com.notMilanGurung.exception.ErrorResponse;
import np.com.notMilanGurung.user.User;

@RestController 
@RequestMapping("/auth")
@Tag(name = "Authentication API", description = "Endpoints for user registration, login, and JWT")
public class AuthController {

  private final AuthService authService;
  
  public AuthController(AuthService authService) {
    this.authService = authService;
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
  public ResponseEntity<Boolean> login(@Valid @RequestBody LoginRequest request) throws ErrorResponse {
    return ResponseEntity.status(200).body(authService.login(request));
  }
}
