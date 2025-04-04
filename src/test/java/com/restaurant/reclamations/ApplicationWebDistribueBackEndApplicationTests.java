package com.restaurant.reclamations;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = ApplicationWebDistribueBackEndApplication.class
)
@TestPropertySource(
    properties = {
        "spring.cloud.config.enabled=false",
        "spring.cloud.discovery.enabled=false",
        "eureka.client.enabled=false"
    }
)
class ApplicationWebDistribueBackEndApplicationTests {

    @Test
    void contextLoads() {
    }

}
