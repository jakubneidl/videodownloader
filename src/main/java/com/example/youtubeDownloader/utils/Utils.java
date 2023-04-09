package com.example.youtubeDownloader.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@Component
public class Utils {
    @Value("${deployed-url:}")
    private String deployedUrl;
    @Value("${server.port}")
    private int serverPort;

    public String getProtocolHostAndPortString() {
        if (deployedUrl != null && !deployedUrl.isEmpty()) {
            return deployedUrl;
        }

        String hostname = "localhost";
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            hostname = inetAddress.getHostName();
        } catch (UnknownHostException e) {
            log.error(e.getMessage());
        }
        return "http://" + hostname + ":" + serverPort;
    }
}
