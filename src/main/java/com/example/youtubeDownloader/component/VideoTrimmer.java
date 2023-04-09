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


        UrlInput input = UrlInput.fromPath(inputPath)
                .setPosition(startTimeSeconds, TimeUnit.SECONDS);

        if (endTimeSeconds != null) {
            long duration = endTimeSeconds - startTimeSeconds;
            input.setDuration(duration, TimeUnit.SECONDS);
        }

        FFmpeg.atPath()
                .addInput(
                        input
                )
                .setOverwriteOutput(true)
                .addOutput(
                        UrlOutput.toPath(outputPath)
                )
                .execute();
    }
}


