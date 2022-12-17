package com.kk.currencyconversionservice;

import feign.Feign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.loadbalancer.RetryableFeignBlockingLoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CurrencyConversionController {

    private final Logger logger = LoggerFactory.getLogger(CurrencyConversionController.class);

    private final CurrencyExchangeProxy currencyExchangeProxy;

    @Autowired
    public CurrencyConversionController(CurrencyExchangeProxy currencyExchangeProxy) {
        this.currencyExchangeProxy = currencyExchangeProxy;
    }

    @GetMapping("/currency-conversion/{from}/{to}/{qty}")
    public CurrencyConversion calculateConversion(@PathVariable String from,
                                                  @PathVariable String to,
                                                  @PathVariable BigDecimal qty) {

        logger.info("calculateConversion {} {} {}", from, to, qty);

        Map<String, String> params = new HashMap<>();
        params.put("from", from);
        params.put("to", to);
        ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class, params);
        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed");
        }
        CurrencyConversion currencyConversion = responseEntity.getBody();
        if (currencyConversion == null) {
            throw new RuntimeException("Not found");
        }
        currencyConversion.setAmount(qty.multiply(currencyConversion.getConversionMultiple()));
        currencyConversion.setQty(qty);
        return currencyConversion;
    }

    @GetMapping("/currency-conversion-feign/{from}/{to}/{qty}")
    public CurrencyConversion calculateConversionFeign(@PathVariable String from,
                                                       @PathVariable String to,
                                                       @PathVariable BigDecimal qty) {
        logger.info("calculateConversionFeign {} {} {}", from, to, qty);

        CurrencyConversion currencyConversion = currencyExchangeProxy.retrieveExchange(from, to);
        if (currencyConversion == null) {
            throw new RuntimeException("Not found");
        }
        currencyConversion.setAmount(qty.multiply(currencyConversion.getConversionMultiple()));
        currencyConversion.setQty(qty);
        return currencyConversion;
    }
}
