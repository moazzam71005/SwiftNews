package com.example.newsapp;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HealthNewsCategory extends Category {

    //private List<Articles> articlesArrayList;
    private TaskCompletedListener listener;
    private final String category = "Health";

    public HealthNewsCategory(TaskCompletedListener listener){
        this.listener = listener;
    }


    @Override
    protected Document doInBackground(String... urls) {

        Document doc = null;
        try {
            doc = Jsoup.connect("https://www.medicalnewstoday.com/").get();
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
            Elements articles = doc.select("a[data-testid='title-link']");
//            Elements headings = doc.select("h3");
//            Elements links = doc.select("h3 a[href]");

            for (int i = 0; i < Math.min(10, articles.size()); i++) {
                Element article = articles.get(i);
                String newsUrl = article.attr("href");
                String title = article.text();
                if (!newsUrl.startsWith("http")) {
                    newsUrl = "https://www.medicalnewstoday.com" + newsUrl;
                }
                MainActivity.articlesArrayList.add(new Articles(title, newsUrl, null));

                // Extract the title and URL of the news article
//                String title = heading.text();
//                String newsUrl = "https://www.aljazeera.com" + link.attr("href");

                // Create an Articles object and add it to the list
                // Add null for imageUrl initially

                // Search for the image URL using the news title
                try {
                    // Execute the SearchImageUrl AsyncTask with the title
                    new SearchImageUrl(new SearchImageUrl.SearchImageUrlCallback() {
                        @Override
                        public void onImageSearchComplete(String imageUrl) {
                            // Handle the image URL here
                            //System.out.println(title);
                            //System.out.println(newsUrl);
                            //System.out.println("Image URL: " + imageUrl);

                            // Update the Articles object with the image URL
                            for (Articles article : MainActivity.articlesArrayList) {
                                if (article.getTitle().equals(title)) {
                                    article.setImageUrl(imageUrl);
                                    count++;
                                    //break;
                                    MainActivity.healthArticles.add(article);

                                }
                                if(listener != null){
                                    //System.out.println("Async Task finished");
                                    if(count == Math.min(10, articles.size())){
                                        listener.onTaskComplete(doc);
                                    }
                                }else{
                                    System.out.println("Listener is null");
                                }


                                //to send signal to main function that task ended
                            }

                        }
                    }).execute(title);

                    //System.out.println("The value of count is " + count);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.e("PakistanNewsCategory", "Document is null");
        }


    }

    public interface NewsFetchListener{ }
}