package com.wpg.demo.spring.cloud.contract.consumer.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@SpringBootApplication
public class DemoSpringCloudContractProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoSpringCloudContractProducerApplication.class, args);
	}
}

@RestController
class FraudController {

	@GetMapping("/frauds")
	ResponseEntity<List<String>> frauds() {
	    return ResponseEntity.status(CREATED).body(Arrays.asList("John", "Michale"));
	}

}
