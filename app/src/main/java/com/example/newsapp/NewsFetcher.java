package com.example.newsapp;

import java.util.List;

public interface NewsFetcher {
    List<Articles> fetchNews();
}
