package com.example.youtubeDownloader.service;

import com.example.youtubeDownloader.component.VideoTrimmer;
import com.github.kiulian.downloader.Config;
import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.request.RequestVideoFileDownload;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import com.github.kiulian.downloader.model.videos.formats.VideoFormat;
import com.github.kiulian.downloader.model.videos.formats.VideoWithAudioFormat;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@Slf4j
public class YoutubeDownloaderService {

    private final YoutubeDownloader downloader;
    private final VideoTrimmer videoTrimmer;

    public YoutubeDownloaderService(VideoTrimmer videoTrimmer) {
        this.videoTrimmer = videoTrimmer;
        this.downloader = new YoutubeDownloader();
    }


//    @SneakyThrows
//    public YoutubeVideo getVideoDetails(String videoId) throws IOException {
//        YoutubeVideo video = downloader.getVideo(videoId);
//        VideoDetails details = video.details();
//        log.info("Video description: {}", details.description());
//        return video;
//    }

    @SneakyThrows
    public void downloadVideo(String videoId, String outputPath, Long startTimeSeconds,
                              Long endTimeSeconds) throws IOException {
        RequestVideoInfo request = new RequestVideoInfo(videoId);
        Response<VideoInfo> response = downloader.getVideoInfo(request);
        VideoInfo video = response.data();
        log.info("Video: {}", video);
        VideoFormat videoFormat = video.bestVideoWithAudioFormat();
        RequestVideoFileDownload requestVideoFileDownload = new RequestVideoFileDownload(videoFormat)
                .saveTo(new File(outputPath));// by default "videos" directory
        Response<File> fileResponse = downloader.downloadVideoFile(requestVideoFileDownload);
        File data = fileResponse.data();

        videoTrimmer.trim(data.toPath(), Path.of(outputPath+"/"+videoId+"_cut.mp4"), startTimeSeconds, endTimeSeconds);
    }


    private Predicate<VideoWithAudioFormat> isMp4() {
        return videoWithAudioFormat -> videoWithAudioFormat.mimeType().contains("mp4");
    }
}
