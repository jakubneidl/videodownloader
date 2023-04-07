package com.example.youtubeDownloader.controller;

import com.example.youtubeDownloader.service.YoutubeDownloaderService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bytedeco.javacpp.indexer.DoubleIndexer;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequiredArgsConstructor
public class YoutubeController {

    private final YoutubeDownloaderService youtubeDownloaderService;

    @SneakyThrows
    @GetMapping("/downloads/{videoId}")
    public ResponseEntity<Resource> downloadVideo(@PathVariable("videoId") String videoId,
                                                  @RequestParam(value = "startTime", required = false) Double startTime,
                                                  @RequestParam(value = "endTime", required = false) Double endTime) {

        String outputPath = "downloads/" + videoId;
        File file = youtubeDownloaderService.downloadVideo(videoId, outputPath, startTime, endTime);

        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("video/mp4"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(new ByteArrayResource(Files.readAllBytes(Path.of(file.getPath()))));
    }
}
