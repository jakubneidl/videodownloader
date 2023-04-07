package com.example.youtubeDownloader.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class VideoCleanupService {

    private final Map<String, Long> fileTimestamps = new ConcurrentHashMap<>();

    public void cleanupOldFiles() {
        int interval = 30 * 60 * 1000;

        long threshold = System.currentTimeMillis() - interval;

//        created 3
//        current 5 - 3
//        delete 6

        Iterator<Map.Entry<String, Long>> iterator = fileTimestamps.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Long> entry = iterator.next();
            if (entry.getValue() < threshold) {
                File file = new File(entry.getKey());
                if (file.delete()) {
                    iterator.remove();
                } else {
                    log.warn("Failed to delete video folder on path: {}", iterator);
                }
            }
        }
    }

    public void addFile(String path) {
        fileTimestamps.put(path, System.currentTimeMillis());
    }
}
