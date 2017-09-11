package com.wpg.demo.spring.demo.spring.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class DemoSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoSpringBootApplication.class, args);
	}
}

@RestController
@RequestMapping("/hello")
class HelloController {

    @RequestMapping("/msg")
    String hello() {
        return "Hello Docker !";
    }

}
