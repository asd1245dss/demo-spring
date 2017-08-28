package com.wpg.demo.spring.springframework.demo02;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

/**
 * @author ChangWei Li
 * @version 2017-08-25 11:31
 */
@Service
@Slf4j
public class Receiver {

    @JmsListener(destination = "emailbox", containerFactory = "mailFactory")
    public void receiveEmailMessage(Email email) {
        log.info("Received < {} >", email);
    }

    @JmsListener(destination = "chatbox", containerFactory = "chartFactory")
    public void receiveChatMessage(String msg) {
        log.info("Received < {} >", msg);
    }

}
