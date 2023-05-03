package com.cabify.carpooling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CarPoolingApplication {

  public static void main(String[] args) {
    SpringApplication.run(CarPoolingApplication.class, args);
  }
}
