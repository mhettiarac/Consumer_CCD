package com.example.consumer;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.assertj.core.api.BDDAssertions;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerRule;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureWireMock (port=8081)
@AutoConfigureStubRunner(
        ids = "com.example:provider:+:stubs:8085",
        stubsMode = StubRunnerProperties.StubsMode.LOCAL)
class ConsumerApplicationTests {

    /** Declaration for Stub **/
/*    @Rule
    public StubRunnerRule stubRunnerRule = new StubRunnerRule()
            .downloadStub("com.example","provider")
            .withPort(8085)
            .stubsMode(StubRunnerProperties.StubsMode.LOCAL);*/

    @Test
    public void unit_test_should_return_all_cities() {

		String json = "[\"Kandy\",\"Colombo\"]";
        WireMock
				.stubFor(WireMock
						.get(WireMock
								.urlEqualTo("/cities"))
						.willReturn(WireMock
								.aResponse()
								.withBody(json)
								.withStatus(200)));

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:8081/cities", String.class);

        BDDAssertions.then(entity.getStatusCodeValue()).isEqualTo(200);
        BDDAssertions.then(entity.getBody()).isEqualTo(json);
    }

    @Test
    public void integration_test_should_return_all_cities() {

        String json = "[\"Kandy\",\"Colombo\"]";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:8082/cities", String.class);

        BDDAssertions.then(entity.getStatusCodeValue()).isEqualTo(200);
        BDDAssertions.then(entity.getBody()).isEqualTo(json);
    }


    @Test
    public void stub_test_should_return_all_cities() {

        String json = "[\"Kandy\",\"Colombo\"]";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:8085/cities", String.class);

        BDDAssertions.then(entity.getStatusCodeValue()).isEqualTo(200);
        BDDAssertions.then(entity.getBody()).isEqualTo(json);
    }


}
