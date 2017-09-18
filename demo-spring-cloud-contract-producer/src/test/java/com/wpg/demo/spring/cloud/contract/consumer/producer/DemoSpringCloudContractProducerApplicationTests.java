package com.wpg.demo.spring.cloud.contract.consumer.producer;

import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;

public class DemoSpringCloudContractProducerApplicationTests {

	@Before
	public void setUp() {
        RestAssuredMockMvc.standaloneSetup(new FraudController());
    }

}
