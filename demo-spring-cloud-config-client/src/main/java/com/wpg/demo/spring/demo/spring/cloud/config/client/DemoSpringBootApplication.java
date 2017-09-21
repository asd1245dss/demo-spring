package com.wpg.demo.spring.demo.spring.cloud.config.client;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.endpoint.ShutdownEndpoint;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.restart.RestartEndpoint;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
public class DemoSpringBootApplication {

    @Bean
    public RestartEndpoint restartEndpoint() {
        return new RestartEndpoint();
    }

    @Bean
    public ShutdownEndpoint shutdownEndpoint(ApplicationContext applicationContext) {
        ShutdownEndpoint shutdownEndpoint = new ShutdownEndpoint();
        shutdownEndpoint.setApplicationContext(applicationContext);

        return shutdownEndpoint;
    }

	public static void main(String[] args) {
		SpringApplication.run(DemoSpringBootApplication.class, args);
	}
}

@ConfigurationProperties
@Data
@Component
class Message {

    private String msg;

}

@RefreshScope
@RestController
@RequestMapping("/hello")
class HelloController {

    @Value("${msg}")
    private String msg;

    private final RestartEndpoint restartEndpoint;

    private final ShutdownEndpoint shutdownEndpoint;

    private final Message message;

    @Autowired
    public HelloController(RestartEndpoint restartEndpoint, ShutdownEndpoint shutdownEndpoint, Message message) {
        this.restartEndpoint = restartEndpoint;
        this.shutdownEndpoint = shutdownEndpoint;
        this.message = message;
    }


    @RequestMapping("/reboot")
    void restart() {
        restartEndpoint.restart();
    }

    @RequestMapping("/shutdown")
    void shutdown() {
        shutdownEndpoint.invoke();
    }

    @RequestMapping("/msg")
    String hello() {
        return String.format("Hello Docker %s %s", message.getMsg(), msg);
    }

}
