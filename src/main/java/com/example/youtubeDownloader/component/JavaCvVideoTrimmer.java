package com.example.youtubeDownloader.component;

import lombok.SneakyThrows;
import org.bytedeco.javacv.*;
import org.springframework.stereotype.Component;

@Component
public class JavaCvVideoTrimmer implements VideoTrimmer {
    private static final boolean AUDIO_ENABLED = true;

    @SneakyThrows
    public void cutVideo(String inputVideoPath,
                         String outputVideoPath,
                         double startTime,
                         double endTime) {
        int audioChannel = 1;

        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputVideoPath);
        grabber.start();

        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputVideoPath, grabber.getImageWidth(), grabber.getImageHeight(), audioChannel);
        recorder.start();

        Frame frame;
        boolean startedRecording = false;

        while ((frame = grabber.grabFrame(true, true, true, false)) != null) {
            double currentTime = grabber.getTimestamp() / 1000000.0; // convert to seconds

            if (currentTime >= startTime && currentTime <= endTime) {
                if (!startedRecording) {
                    recorder.setTimestamp(0); // Reset the timestamp for the output video
                    startedRecording = true;
                }
                recorder.record(frame);
            } else if (currentTime > endTime) {
                break;
            }
        }

        recorder.stop();
        grabber.stop();

    }
}
