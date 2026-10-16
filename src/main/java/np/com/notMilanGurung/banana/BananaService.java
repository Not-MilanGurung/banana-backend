package np.com.notMilanGurung.banana;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service 
public class BananaService {
  
  @Autowired 
  private RestTemplate restTemplate;

  private final String BANANA_API_URL = "https://marcconrad.com/uob/banana/api.php";

  public ResponseEntity<String> fetchAGame(String format, String base64Encoded) {
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(BANANA_API_URL);

    if (format != null && !format.isEmpty()) {
      uriBuilder.queryParam("out", format);
    }

    if (base64Encoded != null && !base64Encoded.isEmpty()) {
      uriBuilder.queryParam("base64", base64Encoded);
    }

    String finalUrl = uriBuilder.toUriString();

    return restTemplate.getForEntity(finalUrl, String.class);
  }
}
