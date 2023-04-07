package com.example.youtubeDownloader.component;

public interface VideoTrimmer {
    void cutVideo(String inputVideoPath,
                  String outputVideoPath,
                  double startTime,
                  double endTime);
}
