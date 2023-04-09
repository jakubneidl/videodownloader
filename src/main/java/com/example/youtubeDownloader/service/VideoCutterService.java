package com.example.youtubeDownloader.service;

import com.example.youtubeDownloader.component.VideoTrimmer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.util.annotation.Nullable;

import java.io.File;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class VideoCutterService {

    private final VideoTrimmer videoTrimmer;

    public File cutVideo(String fileLocation, String videoCutPath, Long startTime, @Nullable Long endTime) {
        videoTrimmer.cutVideo(Path.of(fileLocation), Path.of(videoCutPath), startTime, endTime);
        return new File(videoCutPath);
    }
}
