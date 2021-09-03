package com.mahdi.productmangementservice.resilience;


import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


/*
for whatever reason the controller fail,
the Retry() annotation will retry the call to the controlelr 3 time(default configuration)
*/

@RestController
public class CircuitBreakerController {

    private Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);
    @GetMapping("/sample-api")
    @Retry(name = "sample-api", fallbackMethod = "fallbackmethod")
    public String sampleApi() {
        logger.info("Sample api call received");
        ResponseEntity<String> response = new RestTemplate().getForEntity("http://localhost:8080/okokok", String.class);
        return response.getBody();
    }

    public String fallbackmethod(Exception ex) {
        return "this is the fallback response after 5 failed try to get  response from the @Controller";
    }
}
