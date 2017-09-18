package com.wpg.demo.spring.cloud.eureka.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DemoSpringCloudEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoSpringCloudEurekaServerApplication.class, args);
	}
}
