package ru.moskvin.files;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "ru.moskvin.files.persistence",
        "ru.moskvin.files.configs",
        "ru.moskvin.files.controllers",
        "ru.moskvin.files.models",
        "ru.moskvin.files.services"
})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
