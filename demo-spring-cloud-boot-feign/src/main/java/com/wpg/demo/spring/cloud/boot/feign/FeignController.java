package com.wpg.demo.spring.cloud.boot.feign;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ChangWei Li
 * @version 2017-09-21 09:31
 */
@RestController
public class FeignController {

    private final ConfigClientService configClientService;

    public FeignController(ConfigClientService configClientService) {
        this.configClientService = configClientService;
    }

    @GetMapping("/msg")
    String hello() {
        return configClientService.hello();
    }

}
