package com.alvachien.learning.java.acspringboot2;

import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;  
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/hello2")
public class Hello2Controller {
    protected static Logger logger = LoggerFactory.getLogger(HelloController.class);
    private static final String template = "Hello2, %s! Greeting from Spring-Boot.";
    		
    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody Greeting sayHello(@RequestParam(value="name", required=false, defaultValue="Stranger") String name) {
        return new Greeting(1, String.format(template, name));
    }
}
