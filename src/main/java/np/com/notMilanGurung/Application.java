package np.com.notMilanGurung;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@EnableJpaAuditing 
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}