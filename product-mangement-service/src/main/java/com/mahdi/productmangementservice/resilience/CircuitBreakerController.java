package com.mahdi.productmangementservice.resilience;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


/*
 @Retry: for whatever reason the controller fail,
the Retry() annotation will retry the call to the controlelr 3 time(default configuration)

- @Circuit breaker: the circuit breaker when detects the fail of a service, circuit breaker will invoke the
fallbackMethod without reaching / calling the microservice.
=>it return the default response from the fallbackmethod directly
3 states of the circuit breaker
starts closed than when no proper response are coming from the service
it switches to open to stop all incoming requests and send defaul fall back response instead,
then after a while it move to the semi open state to let only a spécific portion o fthe request to be passed to the microservice
if it detects a healthy response from the service it switchback to closed state,
if not it switch back to closee.
____ __ ____ closeed: closed state, the microservice will be allways called
___ |______ open state: No request are passed to the microservice
___ \_____  semi-open state when the circuit breaker let only on portion of request to be passed to the microservice

@RateLimiter: allow only a certain number of call to the microservice
in a certain amount of time:#rate limiter: allow only 2 requests in 10 seconds

*/

@RestController
public class CircuitBreakerController {

    private Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);

    @GetMapping("/sample-api")
    //@Retry(name = "sample-api", fallbackMethod = "fallbackmethod")
    //@CircuitBreaker(name = "default", fallbackMethod = "fallbackmethod")
    @RateLimiter(name="default")
    public String sampleApi() {
        logger.info("Sample api call received");
        ResponseEntity<String> response = new RestTemplate().getForEntity("http://localhost:8080/okokok", String.class);
        return response.getBody();
    }

    public String fallbackmethod(Exception ex) {
        return "this is the fallback response after 5 failed try to get  response from the @Controller";
    }
}
