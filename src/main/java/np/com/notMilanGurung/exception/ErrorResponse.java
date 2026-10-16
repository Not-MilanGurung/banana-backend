package np.com.notMilanGurung.exception;

import org.springframework.http.HttpStatus;

public class ErrorResponse extends Exception {
  private HttpStatus statusCode;

  public HttpStatus getStatusCode() {return  this.statusCode; }

  public ErrorResponse(HttpStatus statusCode, String message) {
    super(message);
    this.statusCode = statusCode;
  }
}
