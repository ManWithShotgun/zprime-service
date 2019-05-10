package ru.ilia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import ru.ilia.services.PythiaProperties;

@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties(PythiaProperties.class)
@ComponentScan({"ru.ilia.configuration"})
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.run(args);
    }
}
