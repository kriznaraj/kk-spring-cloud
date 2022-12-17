package com.kk.springcloudlimitsservice.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Limits {
    private int min;
    private int max;
}
