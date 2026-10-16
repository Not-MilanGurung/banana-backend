package np.com.notMilanGurung.banana;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping("/banana")
public class BananaController {
  @Autowired 
  private BananaService bananaService;

  @GetMapping("")
  public ResponseEntity<String> getGameData(
    @RequestParam (required = false) String format,
    @RequestParam (required = false) String base64Encoded
  ) {
    return bananaService.fetchAGame(format, base64Encoded);
  }
}
