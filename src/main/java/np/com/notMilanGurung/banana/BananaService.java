package np.com.notMilanGurung.banana;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import np.com.notMilanGurung.exception.ErrorResponse;

@Service 
public class BananaService {
  
  @Autowired 
  private RestTemplate restTemplate;

  private final String BANANA_API_URL = "https://marcconrad.com/uob/banana/api.php";

  public ResponseEntity<String> fetchAGame(String format, String base64Encoded) throws ErrorResponse {
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(BANANA_API_URL);

    if (format != null && !format.isEmpty()) {
      uriBuilder.queryParam("out", format);
    }

    if (base64Encoded != null && !base64Encoded.isEmpty()) {
      uriBuilder.queryParam("base64", base64Encoded);
    }

    String finalUrl = uriBuilder.toUriString();

    try {
      ResponseEntity<String> response = restTemplate.getForEntity(finalUrl, String.class);
      return  response;
    } catch (Exception e) {
      throw new ErrorResponse(HttpStatus.BAD_GATEWAY, "Error connecting to external Banana API");
    }
  }
}
