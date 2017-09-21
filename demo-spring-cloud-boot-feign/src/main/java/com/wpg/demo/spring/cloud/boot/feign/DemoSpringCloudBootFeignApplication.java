package com.wpg.demo.spring.cloud.boot.feign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DemoSpringCloudBootFeignApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoSpringCloudBootFeignApplication.class, args);
	}
}
