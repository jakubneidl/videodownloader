package com.example.youtubeDownloader.wakeup;

import com.example.youtubeDownloader.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class HealthCheckJob {

    private final WebClient webClient;
    private final Utils utils;

    @Scheduled(cron = "0 0 * * * *")
    public void pingHealthCheck() {
        // Send a GET request to the health check endpoint of your application
        String healthCheckUrl = utils.getProtocolHostAndPortString() + "/actuator/health";

        webClient.get()
                .uri(healthCheckUrl)
                .retrieve()
                .toEntity(String.class)
                .doOnSuccess(response -> {
                    HttpStatus statusCode = response.getStatusCode();
                    String body = response.getBody();
                    System.out.println("Health check ping returned status code: " + statusCode);
                    System.out.println("Response body: " + body);
                })
                .block();
    }
}