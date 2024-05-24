package com.example.newsapp;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ReligionNewsCategory extends Category {

    //private List<Articles> articlesArrayList;
    private TaskCompletedListener listener;
    private final String category = "Religion";

    public ReligionNewsCategory(TaskCompletedListener listener){
        this.listener = listener;
    }


    @Override
    protected Document doInBackground(String... urls) {

        Document doc = null;
        try {
            doc = Jsoup.connect("https://www.aljazeera.com/tag/religion/").get();
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
            Elements articles = doc.select("article.gc.u-clickable-card");

            for (int i = 0; i < Math.min(50, articles.size()); i++) {
                Element article = articles.get(i);
                Element headlineElement = article.selectFirst("h3.gc__title a.u-clickable-card__link");
                Element imageElement = article.selectFirst("div.article-card__image-wrap img.article-card__image");

                if (headlineElement != null && imageElement != null) {
                    String newsUrl = headlineElement.attr("href");
                    String title = headlineElement.text();
                    String imageUrl = imageElement.attr("src");

                        // Prepend base URL if image URL is relative
                    if (!imageUrl.startsWith("http")) {
                        imageUrl = "https://www.aljazeera.com" + imageUrl;
                    }

                    MainActivity.articlesArrayList.add(new Articles(title, newsUrl, imageUrl));
                }
                listener(MainActivity.religionArticles, listener, doc);
            }
        } else {
            Log.e("PakistanNewsCategory", "Document is null");
        }


    }

    public interface NewsFetchListener{ }
}