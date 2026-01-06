package istad.co.nectarapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class NectarApiApplication {
    //testing
    public static void main(String[] args) {
        SpringApplication.run(NectarApiApplication.class, args);
    }
}
