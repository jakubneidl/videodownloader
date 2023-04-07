package com.example.youtubeDownloader.service;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.request.RequestVideoFileDownload;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import com.github.kiulian.downloader.model.videos.formats.VideoFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Slf4j
@RequiredArgsConstructor
public class YoutubeDownloaderService {
    private final YoutubeDownloader downloader;

    public File downloadVideo(String videoId, String outputPath) {

        log.info("downloadVideo() with parameters: videoId: {}, outputPath: {}", videoId, outputPath);
        RequestVideoInfo request = new RequestVideoInfo(videoId);
        Response<VideoInfo> response = downloader.getVideoInfo(request);
        VideoInfo video = response.data();

        log.info("Video info: {}, videoId: {}", video, videoId);

        VideoFormat videoFormat = video.bestVideoWithAudioFormat();

        log.info("Best video format: {}, videoId: {}", videoFormat, videoId);

        RequestVideoFileDownload requestVideoFileDownload = new RequestVideoFileDownload(videoFormat)
                .saveTo(new File(outputPath));

        return downloader.downloadVideoFile(requestVideoFileDownload).data();
    }

}
