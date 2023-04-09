package com.example.youtubeDownloader.component;

import com.github.kokorin.jaffree.ffmpeg.FFmpeg;
import com.github.kokorin.jaffree.ffmpeg.UrlInput;
import com.github.kokorin.jaffree.ffmpeg.UrlOutput;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Component
public class VideoTrimmer {
    public void cutVideo(Path inputPath, Path outputPath, Long startTimeSeconds, Long endTimeSeconds) {

        long duration = endTimeSeconds - startTimeSeconds;

        FFmpeg.atPath()
                .addInput(
                        UrlInput.fromPath(inputPath)
                                .setPosition(startTimeSeconds, TimeUnit.SECONDS)
                                .setDuration(duration, TimeUnit.SECONDS)
                )
                .setOverwriteOutput(true)
                .addOutput(
                        UrlOutput.toPath(outputPath)
                )
                .execute();
    }
}


