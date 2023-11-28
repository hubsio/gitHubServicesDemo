package com.example.demo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilderFactory;

@Configuration
@Getter
@Setter
public class AppConfig {
    private static final String GITHUB_API_BASE_URL = "https://api.github.com/";
    private static final String GITHUB_ACCESS_TOKEN = "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";

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(uriBuilderFactory());
        restTemplate.getInterceptors().add(headerInterceptor());
        return restTemplate;
    }

    @Bean
    public UriBuilderFactory uriBuilderFactory() {
        return new DefaultUriBuilderFactory(GITHUB_API_BASE_URL);
    }

    @Bean
    public ClientHttpRequestInterceptor headerInterceptor() {
        return (request, body, execution) -> {
            request.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + GITHUB_ACCESS_TOKEN);
            return execution.execute(request, body);
        };
    }
}
