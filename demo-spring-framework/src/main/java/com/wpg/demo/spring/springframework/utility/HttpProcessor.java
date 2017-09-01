package com.wpg.demo.spring.springframework.utility;

/**
 * @author ChangWei Li
 * @version 2017-09-01 15:42
 */
@FunctionalInterface
public interface HttpProcessor {

    void process(Request request, Response response);

}
