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

        FFmpeg.atPath(Path.of(getFFmpegExecutablePath()))
                .addInput(
                        UrlInput.fromPath(inputPath)
                                .setPosition(startTimeSeconds, TimeUnit.SECONDS)
                                .setDuration(duration, TimeUnit.SECONDS)
                )
                .addArguments("-movflags", "faststart")
                .setOverwriteOutput(true)
                .addOutput(
                        UrlOutput.toPath(outputPath)
                )
                .execute();
    }

    private String getFFmpegExecutablePath() {
        String os = System.getProperty("os.name").toLowerCase();
        String resourceName;

        if (os.contains("win")) {
            resourceName = "/ffmpeg-binaries/windows";
        } else if (os.contains("linux") || os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            resourceName = "/ffmpeg-binaries/linux";
        } else {
            throw new UnsupportedOperationException("Unsupported operating system: " + os);
        }

        try {
            URL resourceUrl = getClass().getResource(resourceName);
            if (resourceUrl == null) {
                throw new FileNotFoundException("FFmpeg binary not found for the current operating system.");
            }

            File ffmpegFile = new File(resourceUrl.toURI());
            return ffmpegFile.getAbsolutePath();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Error converting URL to URI.", e);
        } catch (IOException e) {
            throw new RuntimeException("Error finding FFmpeg executable.", e);
        }
    }

}


