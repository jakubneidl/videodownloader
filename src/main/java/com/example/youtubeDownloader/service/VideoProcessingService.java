package com.example.youtubeDownloader.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
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
        return videoCutterService.cutVideo(file.getPath(), cutVideoPath, startTime, endTime);
    }
}
