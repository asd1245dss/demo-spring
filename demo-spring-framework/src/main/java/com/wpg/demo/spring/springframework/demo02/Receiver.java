package com.wpg.demo.spring.springframework.demo02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

/**
 * @author ChangWei Li
 * @version 2017-08-25 11:31
 */
@Service
public class Receiver {

    private static final Logger logger = LoggerFactory.getLogger(Receiver.class);

    @JmsListener(destination = "mailbox", containerFactory = "mailFactory")
    public void receiveEmailMessage(Email email) {
        logger.info("Received < {} >", email);
    }

    @JmsListener(destination = "chatbox", containerFactory = "chartFactory")
    public void receiveChatMessage(String msg) {
        logger.info("Received < {} >", msg);
    }

}
