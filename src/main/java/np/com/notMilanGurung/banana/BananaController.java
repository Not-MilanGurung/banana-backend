package np.com.notMilanGurung.banana;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@RestController 
@RequestMapping("/banana")
@Validated 
@Tag (name = "Banana API", description = "Endpoint for fetching data from the Banana API")
public class BananaController {

  private BananaService bananaService;

  public BananaController(BananaService bananaService) {
    this.bananaService = bananaService;
  }

  @GetMapping("")
  @Operation(summary = "Fetch game data", description = "Retrieves the game information like image and solution from banana API")
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200", 
      description = "Successfully retrieved data"
    ),
    @ApiResponse(responseCode = "400", description = "Invalid parameter"),
    @ApiResponse(responseCode = "502", description = "Error communicating with Banana API")
  })
  public ResponseEntity<String> getGameData(
    @Parameter(description = "Format of the response: json or csv", example = "json")
    @RequestParam (required = false) 
    @Pattern(regexp = "json|csv", message = "Format must be either 'json' or 'csv'")
    String format,
    
    @Parameter(description = "Whether to return the image url or the base64 encoded version of it")
    @RequestParam (required = false) 
    @Pattern(regexp = "yes|no", message = "The base64Encoding must be either 'yes' or 'no'")
    String base64Encoded
  ) {
    return bananaService.fetchAGame(format, base64Encoded);
  }
}
