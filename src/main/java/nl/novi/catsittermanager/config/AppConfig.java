package nl.novi.catsittermanager.config;

import nl.novi.catsittermanager.mappers.CatMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "nl.novi.catsittermanager")
@ComponentScan(basePackageClasses = AppConfig.class)

public class AppConfig {
    @Bean
    public CatMapper catMapper() {
        return new CatMapper();
    }
}
