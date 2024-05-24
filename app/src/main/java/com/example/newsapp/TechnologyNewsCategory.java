package com.example.newsapp;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class TechnologyNewsCategory extends Category {

    //private List<Articles> articlesArrayList;
    private TaskCompletedListener listener;
    private final String category = "Technology";

    public TechnologyNewsCategory(TaskCompletedListener listener){
        this.listener = listener;
    }


    @Override
    protected Document doInBackground(String... urls) {

        Document doc = null;
        try {
            doc = Jsoup.connect("https://www.cnbc.com/technology/").get();
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
            Elements articles = doc.select("div.Card-standardBreakerCard");
//            Elements headings = doc.select("h3");
//            Elements links = doc.select("h3 a[href]");

            for (int i = 0; i < Math.min(50, articles.size()); i++) {
                Element article = articles.get(i);
                Element headlineElement = article.selectFirst("div.Card-titleContainer a.Card-title");
                Element imageElement = article.selectFirst("img.Card-mediaContainerInner");
                String newsUrl;
                if(headlineElement == null){
                    continue;
                }else{
                    newsUrl = headlineElement.attr("href");
                }

                String title = article.text();
                String imageUrl = "";
                if (imageElement != null) {
                    imageUrl = imageElement.attr("src");
                } else {
                    // Skip this element if no image is found
                    continue;
                }
                MainActivity.articlesArrayList.add(new Articles(title, newsUrl, imageUrl));


            }
            System.out.println("size " + MainActivity.articlesArrayList.size());

            listener(MainActivity.technologyArticles, listener, doc);

        } else {
            Log.e("PakistanNewsCategory", "Document is null");
        }


    }

    public interface NewsFetchListener{ }
}