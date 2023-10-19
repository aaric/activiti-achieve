package com.at7.flow;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Flow Application
 *
 * @author Aaric
 * @version 0.1.0-SNAPSHOT
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Online Api Document",
                description = "Activiti 7.4 Learning",
                version = "0.1.0",
                contact = @Contact(
                        name = "Aaric", url = "https://github.com/aaric"
                )
        )
)
@SpringBootApplication
public class FlowApp {

    public static void main(String[] args) {
        SpringApplication.run(FlowApp.class, args);
    }
}
