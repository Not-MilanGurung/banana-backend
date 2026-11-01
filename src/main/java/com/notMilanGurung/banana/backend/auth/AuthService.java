package com.notMilanGurung.banana.backend.auth;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.notMilanGurung.banana.backend.exception.ErrorResponse;
import com.notMilanGurung.banana.backend.user.User;
import com.notMilanGurung.banana.backend.user.UserRepository;

@Service
public class AuthService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * Register a new user
   * 
   * @param request
   * @return user
   * @throws ErrorResponse
   */
  public User register(RegisterRequest request) throws ErrorResponse {
    if (userRepository.findByUsername(request.getUsername()).isPresent()) {
      throw new ErrorResponse(HttpStatus.BAD_REQUEST, "Username is already taken!");
    }

    User user = new User();
    user.setUsername(request.getUsername());
    user.setEmail(request.getEmail());
    user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

    return userRepository.save(user);
  }

  /**
   * Login for user currently (currently only return true)
   * 
   * @param request
   * @return true
   * @throws ErrorResponse
   */
  public User login(LoginRequest request) throws ErrorResponse {
    User user = userRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new ErrorResponse(HttpStatus.BAD_REQUEST, "Invalid username or password"));

    if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
      throw new ErrorResponse(HttpStatus.BAD_REQUEST, "Invalid username or password");
    }
    return user;
  }

}
