package com.alvachien.learning.java.acspringboot2;

import org.springframework.boot.SpringApplication;  
import org.springframework.boot.autoconfigure.SpringBootApplication;  
  
@SpringBootApplication(scanBasePackages={"com.alvachien.learning.java.acspringboot2"})
public class Application {
    public static void main(String[] args) {  
        SpringApplication.run(Application.class, args);  
    }
}
