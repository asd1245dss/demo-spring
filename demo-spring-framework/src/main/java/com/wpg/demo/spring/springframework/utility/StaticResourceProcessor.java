package com.wpg.demo.spring.springframework.utility;

/**
 * @author ChangWei Li
 * @version 2017-09-01 15:41
 */
public class StaticResourceProcessor implements HttpProcessor {

    @Override
    public void process(Request request, Response response) {
        response.sendStaticResource();
    }

}
