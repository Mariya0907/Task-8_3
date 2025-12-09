package com.reviewsfx.config;

import org.springframework.web.client.RestTemplate;

public class RestTemplateConfig {

    public static RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

