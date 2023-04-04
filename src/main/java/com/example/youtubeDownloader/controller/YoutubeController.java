package com.example.youtubeDownloader.controller;

import com.example.youtubeDownloader.service.YoutubeDownloaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class YoutubeController {

    private final YoutubeDownloaderService youtubeDownloaderService;

    @GetMapping("/downloads/{videoId}")
    public ResponseEntity<String> downloadVideo(@PathVariable("videoId") String videoId,
                                                @RequestParam("startTime") Long startTime,
                                                @RequestParam("endTime") Long endTime) {
        try {
            String outputPath = "downloads/" + videoId;
            youtubeDownloaderService.downloadVideo(videoId, outputPath, startTime, endTime);
            return ResponseEntity.ok("Video downloaded successfully to " + outputPath);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error downloading video: " + e.getMessage());
        }
    }
}
