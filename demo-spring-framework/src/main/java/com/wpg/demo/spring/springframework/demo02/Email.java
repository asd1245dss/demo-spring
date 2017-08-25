package com.wpg.demo.spring.springframework.demo02;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author ChangWei Li
 * @version 2017-08-25 11:31
 */
@Data
@AllArgsConstructor
class Email {

    private String to;

    private String body;

}
