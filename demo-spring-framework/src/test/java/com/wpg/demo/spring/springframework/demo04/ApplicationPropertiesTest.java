package com.wpg.demo.spring.springframework.demo04;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author ChangWei Li
 * @version 2017-08-28 11:24
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationProperties.class)
@TestPropertySource(properties = {"app.name=test-demo-spring"})
@Slf4j
public class ApplicationPropertiesTest {

    @Autowired
    ApplicationProperties applicationProperties;

    @Test
    public void read_env_properties() {
        String expectedAppName = "test-demo-spring";

        log.info("application => {}", applicationProperties);

        assertEquals(expectedAppName, applicationProperties.getName());
    }

}