package com.example.youtubeDownloader.service;

import com.example.youtubeDownloader.component.VideoTrimmer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class VideoCutterService {

    private final VideoTrimmer videoTrimmer;

    public File cutVideo(String fileLocation, String videoCutPath, Long startTime, Long endTime) {
        videoTrimmer.cutVideo(Path.of(fileLocation), Path.of(videoCutPath), startTime, endTime);
        return new File(videoCutPath);
    }
}
