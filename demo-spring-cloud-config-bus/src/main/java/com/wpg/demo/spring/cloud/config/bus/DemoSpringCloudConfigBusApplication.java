package com.wpg.demo.spring.cloud.config.bus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DemoSpringCloudConfigBusApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoSpringCloudConfigBusApplication.class, args);
	}
}
