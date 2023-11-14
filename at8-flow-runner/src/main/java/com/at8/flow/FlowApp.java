package com.at8.flow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Flow Application
 *
 * @author Aaric
 * @version 0.4.0-SNAPSHOT
 */
//@OpenAPIDefinition(
//        info = @Info(
//                title = "Online Api Document",
//                description = "Activiti 8.0 Learning",
//                version = "0.1.0",
//                contact = @Contact(
//                        name = "Aaric", url = "https://github.com/aaric"
//                )
//        )
//)
@SpringBootApplication
public class FlowApp {

    public static void main(String[] args) {
        SpringApplication.run(FlowApp.class, args);
    }
}
