package com.example.youtubeDownloader.service;

import com.example.youtubeDownloader.component.VideoTrimmer;
import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.request.RequestVideoFileDownload;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import com.github.kiulian.downloader.model.videos.formats.VideoFormat;
import com.github.kiulian.downloader.model.videos.formats.VideoWithAudioFormat;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.function.Predicate;

@Service
@Slf4j
@AllArgsConstructor
public class YoutubeDownloaderService {

    private final YoutubeDownloader downloader;
    private final VideoTrimmer videoTrimmer;


    @SneakyThrows
    public File downloadVideo(String videoId, String outputPath, Double startTimeSeconds,
                              Double endTimeSeconds) throws IOException {
        boolean trimVideo = true;
        RequestVideoInfo request = new RequestVideoInfo(videoId);
        Response<VideoInfo> response = downloader.getVideoInfo(request);


        VideoInfo video = response.data();


        if (startTimeSeconds == null && endTimeSeconds == null) {
            trimVideo = false;
        }

        if (startTimeSeconds == null) {
            startTimeSeconds = 0D;
        }
        if (endTimeSeconds == null) {
            endTimeSeconds = (double) video.details().lengthSeconds();
        }

        log.info("Video: {}", video);
        VideoFormat videoFormat = video.bestVideoWithAudioFormat();
        RequestVideoFileDownload requestVideoFileDownload = new RequestVideoFileDownload(videoFormat)
                .saveTo(new File(outputPath));// by default "videos" directory
        Response<File> fileResponse = downloader.downloadVideoFile(requestVideoFileDownload);
        File data = fileResponse.data();

        String cutVideoOutputPath = outputPath + "/" + videoId + "_cut.mp4";
        if (trimVideo) {
            videoTrimmer.cutVideo(data.getPath(), cutVideoOutputPath, startTimeSeconds, endTimeSeconds);
        }

        return new File(cutVideoOutputPath);
    }


    private Predicate<VideoWithAudioFormat> isMp4() {
        return videoWithAudioFormat -> videoWithAudioFormat.mimeType().contains("mp4");
    }
}
