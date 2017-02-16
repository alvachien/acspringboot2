package com.alvachien.learning.java.acspringboot2;

import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;  
import org.springframework.web.bind.annotation.PathVariable;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RestController; 

public class HelloController {
    protected static Logger logger=LoggerFactory.getLogger(HelloController.class);  
    
    @RequestMapping("/")  
    public String helloworld(){  
        logger.debug("∑√Œ hello");  
        return "Hello world from Spring Boot!";  
    }  
      
    @RequestMapping("/hello/{name}")  
    public String helloName(@PathVariable String name){  
        logger.debug("∑√Œ helloName,Name={}",name);  
        return "Hello "+name + " from Spring Boot!";  
    }
}
