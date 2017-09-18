package com.wpg.demo.spring.cloud.contract.consumer;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.assertj.core.api.BDDAssertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerRule;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWireMock(port = 8181)
public class DemoSpringCloudContractApplicationTests {

    @Rule
    public StubRunnerRule stubRunnerRule = new StubRunnerRule()
            .downloadStub("com.wpg.demo.spring.cloud", "demo-spring-cloud-contract-producer", "0.0.1-SNAPSHOT")
            .workOffline(true)
            .withPort(8183);

	@Test
	public void test_should_return_all_frauds() {

        String json = "[\"John\",\"Michale\"]";

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/frauds"))
                .willReturn(WireMock.aResponse().withBody(json).withStatus(201)));


        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:8080/frauds", String.class);

        BDDAssertions.then(entity.getStatusCodeValue()).isEqualTo(201);
        BDDAssertions.then(entity.getBody()).isEqualTo(json);
	}

    @Test
    public void test_should_return_all_frauds_integration() {

        String json = "[\"John\",\"Michale\"]";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:8080/frauds", String.class);

        BDDAssertions.then(entity.getStatusCodeValue()).isEqualTo(201);
        BDDAssertions.then(entity.getBody()).isEqualTo(json);
    }

    @Test
    public void test_should_return_all_frauds_stub() {

        String json = "[\"John\",\"Michale\"]";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:8183/frauds", String.class);

        BDDAssertions.then(entity.getStatusCodeValue()).isEqualTo(201);
        BDDAssertions.then(entity.getBody()).isEqualTo(json);
    }

}
