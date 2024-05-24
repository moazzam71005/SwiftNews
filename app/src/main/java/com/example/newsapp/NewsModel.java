package com.example.newsapp;

import java.util.ArrayList;

public class NewsModel {
    private int totalResults;

    private ArrayList<Articles> articles;

    public NewsModel(int totalResults, ArrayList<Articles> articles) {
        this.totalResults = totalResults;
        this.articles = articles;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public ArrayList<Articles> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<Articles> articles) {
        this.articles = articles;
    }
}
