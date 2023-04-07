package com.example.youtubeDownloader.controller;

import com.example.youtubeDownloader.excetion.InvalidTimeIntervalException;
import com.example.youtubeDownloader.service.VideoCleanupService;
import com.example.youtubeDownloader.service.VideoProcessingService;
import com.example.youtubeDownloader.uitls.YoutubeVideoIdExtractor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequiredArgsConstructor
public class YoutubeController {

    private final VideoProcessingService videoProcessingService;
    private final VideoCleanupService cleanupService;

    @SneakyThrows
    @GetMapping("/api/youtube/v1/downloads")
    public ResponseEntity<Resource> downloadVideo(@RequestParam("videoLink") String videoLink,
                                                  @RequestParam(value = "startTime", required = false) Long startTime,
                                                  @RequestParam(value = "endTime", required = false) Long endTime) {


        if (startTime != null && endTime != null && (startTime > endTime)) {
            String message = "Start time cannot be larger than end time. startTime: " + startTime + " endTime: " + endTime;
            throw new InvalidTimeIntervalException(message);
        }

        String videoId = YoutubeVideoIdExtractor.extractVideoId(videoLink);
        Path tempDirectory = Files.createTempDirectory("downloads-");
        String outputDir = tempDirectory + videoId;
        File file = videoProcessingService.downloadAndCut(videoId, outputDir, startTime, endTime);

        cleanupService.addFile(outputDir);

        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("video/mp4"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(new ByteArrayResource(Files.readAllBytes(Path.of(file.getPath()))));
    }
}
