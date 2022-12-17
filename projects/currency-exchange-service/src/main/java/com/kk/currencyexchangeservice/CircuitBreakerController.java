package com.kk.currencyexchangeservice;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CircuitBreakerController {

    private final Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);

    @GetMapping("/sample-api")
//    @Retry(name = "sample-api", fallbackMethod = "defaultSampleApiResponseFn")
    @CircuitBreaker(name = "default", fallbackMethod = "defaultSampleApiResponseFn")
    @RateLimiter(name = "default")
    @Bulkhead(name = "default")
    public String sampleApi() {
        logger.info("sample-api invoked");
        //We know this would fail
        ResponseEntity<String> forEntity = new RestTemplate().getForEntity("http://someinvalidurl.com/get-something", String.class);
        return forEntity.getBody();
    }

    private String defaultSampleApiResponseFn(Exception ex) {
        return "default-sample-api-response";
    }

}
