package nl.novi.catsittermanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ComponentScan(basePackages = {"nl.novi.catsittermanager"})
@SpringBootApplication
@EnableTransactionManagement
public class CatsittermanagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatsittermanagerApplication.class, args);
    }
}
