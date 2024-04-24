package nl.novi.catsittermanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"nl.novi.catsittermanager"})
public class CatsittermanagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CatsittermanagerApplication.class, args);
    }
}
