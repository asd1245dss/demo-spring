package com.wpg.demo.spring.springframework.demo04;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author ChangWei Li
 * @version 2017-08-28 10:55
 */
@Configuration
@ComponentScan
@Slf4j
public class PropertyApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(PropertyApplication.class);

        log.debug("app.name => {}", applicationContext.getEnvironment().getProperty("app.profile"));

        ApplicationProperties applicationProperties = applicationContext.getBean(ApplicationProperties.class);

        log.debug("app => {}", applicationProperties);
    }

}
