package com.wpg.demo.spring.cloud.boot.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author ChangWei Li
 * @version 2017-09-21 09:28
 */
@FeignClient("configClient")
public interface ConfigClientService {

    @GetMapping("/hello/msg")
    String hello();

}
