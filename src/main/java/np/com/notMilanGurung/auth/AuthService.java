package np.com.notMilanGurung.auth;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import np.com.notMilanGurung.exception.ErrorResponse;
import np.com.notMilanGurung.user.User;
import np.com.notMilanGurung.user.UserRepository;

@Service 
public class AuthService {
  
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;
  private final int bcryptRounds = 10;

  public AuthService(UserRepository userRepository) {
    this.userRepository = userRepository;
    this.passwordEncoder = new BCryptPasswordEncoder(bcryptRounds);
  }

  /**
   * Register a new user
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
   * @param request
   * @return true
   * @throws ErrorResponse
   */
  public boolean login(LoginRequest request) throws ErrorResponse {
    User user = userRepository.findByUsername(request.getUsername())
                  .orElseThrow(() -> new ErrorResponse(HttpStatus.BAD_REQUEST, "Invalid username or password"));
    final boolean passwordMatch = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());
    if (!passwordMatch) {
      throw new ErrorResponse(HttpStatus.BAD_REQUEST, "Invalid username or password");
    } 

    return passwordMatch;
  }

}
