package com.example.youtubeDownloader.excetion;

import org.springframework.http.HttpStatus;

public class InvalidTimeIntervalException extends VideoDownloaderException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;

    public InvalidTimeIntervalException(String message) {
        super(message, HTTP_STATUS);
    }
}
