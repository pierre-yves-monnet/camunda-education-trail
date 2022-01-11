package main.java.org.camunda.educational;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.camunda.bpm.spring.boot.starter.telemetry.CamundaApplicationServerConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class educationalTrail {
    public static void main(String... args) {
        SpringApplication.run(educationalTrail.class, args);
    }

}
