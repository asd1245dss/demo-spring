package com.wpg.demo.spring.springframework.demo01;

import org.springframework.stereotype.Component;

/**
 * @author ChangWei Li
 * @version 2017-08-25 09:26
 */
@Component
class MessagePrinter {

    private final MessageService messageService;

    MessagePrinter(MessageService messageService) {
        this.messageService = messageService;
    }

    void printMessage() {
        System.out.printf("current class is %s\n", messageService.getMessage());
    }

}
