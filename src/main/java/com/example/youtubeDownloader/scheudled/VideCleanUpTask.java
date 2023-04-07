package com.example.youtubeDownloader.scheudled;

import com.example.youtubeDownloader.service.VideoCleanupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class VideCleanUpTask {
    @Autowired
    private VideoCleanupService videoCleanupService;

    @Scheduled(fixedRate = 30 * 60 * 1000) // every 30 minutes
    public void cleanupOldVideos() {
        videoCleanupService.cleanupOldFiles();
    }
}
