package com.example.youtubeDownloader.excetion;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class VideoDownloaderException extends RuntimeException {
    private final HttpStatus httpStatus;

    public VideoDownloaderException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
