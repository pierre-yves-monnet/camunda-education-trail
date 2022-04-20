package org.camunda.educational;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableProcessApplication

public class EducationalTrail {
    public static void main(String... args) {
        SpringApplication.run(EducationalTrail.class, args);
    }

}
