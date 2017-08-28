package com.wpg.demo.spring.springframework.demo04;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 *
 * <pre>
 *     @value could only process string based value or you need to create custom converter
 * </pre>
 *
 * @author ChangWei Li
 * @version 2017-08-28 10:58
 */
@Component
@PropertySource("classpath:/application.properties")
@Data
public class ApplicationProperties {

    @Value("${app.name}")
    private String name;

    @Value("${app.profile}")
    private String[] configs;

    @Value("${app.version}")
    private String version;

    @Value("${app.relase.date}")
    private String date;

}
