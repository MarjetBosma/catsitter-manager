package nl.novi.catsittermanager;

import nl.novi.catsittermanager.config.JacksonConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan(basePackages = {"nl.novi.catsittermanager"})
@SpringBootApplication
public class CatsittermanagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CatsittermanagerApplication.class, args);
    }
}
