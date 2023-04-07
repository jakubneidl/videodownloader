package com.example.youtubeDownloader.uitls;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeVideoIdExtractor {
    private static final String regex = "(?<=v=|v/|embed/|youtu.be/)[A-Za-z0-9_-]{11}";
    private static final Pattern pattern = Pattern.compile(regex);

    public static String extractVideoId(String youtubeLink) {
        String videoId = "";
        if (youtubeLink != null && youtubeLink.trim().length() > 0) {
            Matcher matcher = pattern.matcher(youtubeLink);
            if (matcher.find()) {
                videoId = matcher.group();
            }
        }
        return videoId;
    }
}
