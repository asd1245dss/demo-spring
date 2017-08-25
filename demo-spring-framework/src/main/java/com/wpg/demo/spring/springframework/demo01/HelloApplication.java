package com.wpg.demo.spring.springframework.demo01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author ChangWei Li
 * @version 2017-08-25 09:25
 */
@Configuration
@ComponentScan
class HelloApplication {

    private static final Logger logger = LoggerFactory.getLogger(HelloApplication.class);

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(HelloApplication.class);

        logger.debug("Springframework container refresh successfully !");

        MessagePrinter messagePrinter = applicationContext.getBean(MessagePrinter.class);
        messagePrinter.printMessage();

        logger.debug("Springframework container destroy successfully !");
    }

}
