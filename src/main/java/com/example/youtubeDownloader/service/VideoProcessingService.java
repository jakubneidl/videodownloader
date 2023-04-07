package com.example.youtubeDownloader.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoProcessingService {
    private final YoutubeDownloaderService youtubeDownloaderService;
    private final VideoCutterService videoCutterService;

    public File downloadAndCut(String videoId, String outputFolderPath, Long startTime, Long endTime) {
        File file = youtubeDownloaderService.downloadVideo(videoId, outputFolderPath);
        if (startTime == null && endTime == null) {
            return file;
        }

        if (startTime == null) {
            startTime = 0L;
        }
        String cutVideoPath = outputFolderPath + videoId + "_cut.mp4";
        File videoCutFile = videoCutterService.cutVideo(file.getPath(), cutVideoPath, startTime, endTime);

        if (file.delete()) {
            log.info("Original file deleted successfully. VideoId: {}", videoId);
        }
        return videoCutFile;
    }
}
