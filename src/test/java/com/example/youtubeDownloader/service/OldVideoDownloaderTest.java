package com.example.youtubeDownloader.service;

import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class OldVideoDownloaderTest {

    private OldVideoDownloader oldVideoDownloader = new OldVideoDownloader();

    @Test
    void downloadVideo() throws IOException {
        String videoId = "VIDEO_ID";
        String metadataUrl = "https://www.youtube.com/watch?v=4qNwoAAfnk4&ab_channel=WebDevSimplified" + videoId;
        HttpURLConnection connection = (HttpURLConnection) new URL(metadataUrl).openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String metadata = reader.readLine();
        reader.close();

        Pattern urlPattern = Pattern.compile("url_encoded_fmt_stream_map=(.*)&");
        Matcher matcher = urlPattern.matcher(metadata);

        if (matcher.find()) {
            String encodedUrls = java.net.URLDecoder.decode(matcher.group(1), "UTF-8");
            System.out.println(encodedUrls);
        }
    }
    @Test
    void test2() throws IOException {
        String videoId = "4qNwoAAfnk4";
        String videoUrl = "https://www.youtube.com/watch?v=" + videoId;

        // Step 1: Send an HTTP GET request to the video page to get the HTML response
        HttpURLConnection connection = (HttpURLConnection) new URL(videoUrl).openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder html = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            html.append(line);
        }

        reader.close();
        connection.disconnect();

//        System.out.println(html.toString());

        String regex = "\\bhttps://\\S+";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(html);

        while (matcher.find()) {
            String link = matcher.group();
            System.out.println(link);
        }


//        // Step 2: Parse the HTML to extract the video URL
//        Pattern pattern = Pattern.compile("\"url_encoded_fmt_stream_map\":\"([^\"]+)\"");
//        Matcher matcher = pattern.matcher(html.toString());


        String encodedStreamMap = "";
        if (matcher.find()) {
            encodedStreamMap = matcher.group(1);
        }

        String streamMap = URLDecoder.decode(encodedStreamMap, "UTF-8");

        String[] videoUrls = streamMap.split(",");
        String videoStreamUrl = null;

        for (String url : videoUrls) {
            if (url.contains("itag=22")) {
                videoStreamUrl = url.replace("\\u0026", "&").replace("\"", "").replace("url=", "");
                break;
            }
        }

        if (videoStreamUrl == null) {
            throw new RuntimeException("Could not find video stream URL");
        }

        // Step 3: Send an HTTP GET request to the video stream URL to get the video stream
        connection = (HttpURLConnection) new URL(videoStreamUrl).openConnection();
        connection.setRequestMethod("GET");

        BufferedInputStream streamReader = new BufferedInputStream(connection.getInputStream());
        FileOutputStream fileWriter = new FileOutputStream("video.mp4");

        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = streamReader.read(buffer)) != -1) {
            fileWriter.write(buffer, 0, bytesRead);
        }

        streamReader.close();
        fileWriter.close();
        connection.disconnect();

        System.out.println("Video downloaded successfully!");

    }
}