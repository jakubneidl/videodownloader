package com.example.youtubeDownloader.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewsController {
    @GetMapping("/")
    public String getHomePage() {
        return "index.html";
    }
}
