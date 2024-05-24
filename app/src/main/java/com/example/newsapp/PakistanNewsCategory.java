package com.example.newsapp;


import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class PakistanNewsCategory extends Category {



    //private List<Articles> articlesArrayList;
    private TaskCompletedListener listener;
    private boolean initiated = false;

    public PakistanNewsCategory(TaskCompletedListener listener){
        this.listener = listener;

    }

    public boolean getInitiated(){
        return this.initiated;
    }


    public void setInitiated(){
        this.initiated = true;
    }

    @Override
    protected Document doInBackground(String... urls) {



        Document doc = null;
        try {
            doc = Jsoup.connect("https://www.dawn.com/latest-news").get();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Index out of bounds");
        }

        return doc;
    }



    int count = 0;
    @Override
    protected void onPostExecute(Document doc) {
        if (doc != null) {
            // Execution of result here
            Elements articles = doc.select("div.flex.flex-col.sm\\:flex-row");
            Set<String> processedUrls = new HashSet<>();

            for (int i = 0; i < Math.min(50, articles.size()); i++) {
                Element article = articles.get(i);
                Element headlineElement = article.selectFirst("h2.story__title a.story__link");
                Element imageElement = article.selectFirst("figure.media img");

                if (headlineElement != null && imageElement != null) {
                    String newsUrl = headlineElement.attr("href");
                    String title = headlineElement.text();
                    if (processedUrls.contains(newsUrl)) {
                        continue;
                    }
                    processedUrls.add(newsUrl);
                    String imageUrl = "";
                    if (imageElement != null) {
                        imageUrl = imageElement.attr("data-src"); // Try data-src first
                        if (imageUrl.isEmpty()) {
                            imageUrl = imageElement.attr("src"); // Fall back to src
                        }
                    }
                    MainActivity.articlesArrayList.add(new Articles(title, newsUrl, imageUrl));
                    //count++;
                }
                listener(MainActivity.pakistanArticles, listener, doc);

            }
        } else {
            Log.e("PakistanNewsCategory", "Document is null");
        }


    }

    public interface NewsFetchListener{
}
}