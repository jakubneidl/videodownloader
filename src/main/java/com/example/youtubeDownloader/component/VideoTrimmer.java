package com.example.youtubeDownloader.component;

import com.github.kokorin.jaffree.StreamType;
import com.github.kokorin.jaffree.ffmpeg.*;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class VideoTrimmer {
    public void trim(Path inputPath, Path outputPath, Long startTimeSeconds, Long endTimeSeconds) {

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


