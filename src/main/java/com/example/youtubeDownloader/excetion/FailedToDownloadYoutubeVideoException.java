package com.example.youtubeDownloader.excetion;

import org.springframework.http.HttpStatus;

public class FailedToDownloadYoutubeVideoException extends VideoDownloaderException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    public FailedToDownloadYoutubeVideoException(String message) {
        super(message, HTTP_STATUS);
    }
}
