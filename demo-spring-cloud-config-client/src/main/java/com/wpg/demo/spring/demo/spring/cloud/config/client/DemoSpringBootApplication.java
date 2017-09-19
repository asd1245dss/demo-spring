package com.wpg.demo.spring.demo.spring.cloud.config.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
public class DemoSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoSpringBootApplication.class, args);
	}
}

@RestController
@RequestMapping("/hello")
class HelloController {

    @Value("${msg:default Message}")
    private String msg;

    @RequestMapping("/msg")
    String hello() {
        return String.format("Hello Docker %s!", msg);
    }

}
