package com.prestamosfacil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PrestamosFacilApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrestamosFacilApiApplication.class, args);
    }
}