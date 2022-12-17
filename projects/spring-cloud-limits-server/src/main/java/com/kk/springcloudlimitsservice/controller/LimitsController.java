package com.kk.springcloudlimitsservice.controller;

import com.kk.springcloudlimitsservice.bean.Limits;
import com.kk.springcloudlimitsservice.config.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LimitsController {

    private final Configuration configuration;

    @Autowired
    public LimitsController(Configuration configuration) {
        this.configuration = configuration;
    }

    @GetMapping("/limits")
    public Limits retrieveLimits() {
        return new Limits(configuration.getMin(), configuration.getMax());
    }
}
