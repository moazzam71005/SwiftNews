package com.example.newsapp;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class WorldNewsCategory extends Category {

    //private List<Articles> articlesArrayList;
    private TaskCompletedListener listener;
    private final String category = "World";

    public WorldNewsCategory(TaskCompletedListener listener){
        this.listener = listener;
    }


    @Override
    protected Document doInBackground(String... urls) {

        Document doc = null;
        try {
            doc = Jsoup.connect("https://www.ndtv.com/world-news").get();
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
            Elements articles = doc.select("div.news_Itm");
//            Elements headings = doc.select("h3");
//            Elements links = doc.select("h3 a[href]");

            for (int i = 0; i < Math.min(50, articles.size()); i++) {
                Element article = articles.get(i);
                Element headlineElement = article.selectFirst("h2.newsHdng a");
                Element imageElement = article.selectFirst("div.news_Itm-img a img");
                if (headlineElement != null && imageElement != null) {
                    String newsUrl = headlineElement.attr("href");
                    String title = headlineElement.text();
                    String imageUrl = imageElement.attr("src");
                    MainActivity.articlesArrayList.add(new Articles(title, newsUrl, imageUrl));
                }
//                if(MainActivity.articlesArrayList.size() == 10){
//                    MainActivity.worldArticles.addAll(MainActivity.articlesArrayList);
//                    listener.onTaskComplete(doc);
//                }
                listener(MainActivity.worldArticles, listener, doc);


                // Extract the title and URL of the news article
//                String title = heading.text();
//                String newsUrl = "https://www.aljazeera.com" + link.attr("href");

                // Create an Articles object and add it to the list
                // Add null for imageUrl initially

                // Search for the image URL using the news title

            }
        } else {
            Log.e("PakistanNewsCategory", "Document is null");
        }


    }

    public interface NewsFetchListener{ }
}