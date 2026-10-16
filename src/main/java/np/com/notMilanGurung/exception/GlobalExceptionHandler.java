package np.com.notMilanGurung.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Catches validation failures on query parameters (e.g., @Size, @Pattern)
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
    Map<String, Object> response = new HashMap<>();

    // Extract readable messages from violations
    String errors = ex.getConstraintViolations().stream()
        .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
        .collect(Collectors.joining(", "));

    response.put("error", "Validation Failed");
    response.put("message", errors);

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * Catches invalid enum values or wrong data types passed into parameters
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    Map<String, Object> response = new HashMap<>();

    String message = String.format("Parameter '%s' has an invalid value: '%s'. Please provide a valid value.",
        ex.getName(), ex.getValue());

    response.put("error", "Invalid Parameter Value");
    response.put("message", message);

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * Catches all the other expections
   */
  @ExceptionHandler(ErrorResponse.class)
  public ResponseEntity<Map<String, Object>> handleError(ErrorResponse ex) {
    Map<String, Object> response = new HashMap<>();

    response.put("error", "Error Encountered");
    response.put("message", ex.getMessage());

    return  new ResponseEntity<>(response, ex.getStatusCode());
  }


}
