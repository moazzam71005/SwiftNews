package com.example.newsapp;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SearchImageUrl extends AsyncTask<String, Void, String> {



    public String imageUrl; // Variable to store the fetched image URL
    private SearchImageUrlCallback callback;
    public static String imageURL;

    private static String[] apiKey = {"AIzaSyADaugNpe2D3A2CjbqOQSRfEbDRVEVBrGM", "AIzaSyDqNmwUiSE6vZXhdS7UmQ-4W1mJlsxzhvU", " AIzaSyCHqDDLnooJs4iHAdb775YZtS-WJK587bo", "AIzaSyD3qEp0z_W9TDu0U7opP0YuZjhuRFmN414", "AIzaSyD1vA9PugwjxwkGUwkbqc90i5RqbCRxSQs", "AIzaSyCoxDo5XYEpVt5tJRd4wUP7UdYPGeWurHU", "AIzaSyAf1nHAGpsOyeZLqmoWLoCju7tHxsIAejA", "AIzaSyDQ-xhO0MH2UP2vBKQEMeQ21IAhRhivmQ8", "AIzaSyDFNot8pX1tDM1UcSIV41uSVdYWjq7AO24", "AIzaSyBDZ44G4w8adD_X23zCK1xCag8GrDLwTlg"};
    private static final String cx = "3332a912039a34547";;

    // Define a callback interface to handle the result
    public interface SearchImageUrlCallback {
        void onImageSearchComplete(String imageUrl);
    }

    public SearchImageUrl(SearchImageUrlCallback callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... queries) {
        int max = 9;
        int min = 0;
        int range = max - min + 1;
        int rand = (int)(Math.random() * range) + min;


        if (queries.length == 0) {
            return null;
        }
        String query = queries[0];
        try {
            // Encode the query string
            String encodedQuery = URLEncoder.encode(query, "UTF-8");

            // Construct the URL for the API request
            URL url = new URL("https://www.googleapis.com/customsearch/v1?key=" + apiKey[rand] +
                    "&cx=" + cx + "&q=" + encodedQuery + "&searchType=image");

            // Open connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Get the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse JSON response
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray items = jsonResponse.getJSONArray("items");

            // Return the image URL of the first search result
            if (items.length() > 0) {
                imageUrl = items.getJSONObject(0).getString("link"); // Store the fetched image URL in the variable

                return imageUrl;
            } else {
                return "No images found";
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (callback != null) {
            callback.onImageSearchComplete(result); // Pass the fetched image URL to the callback method
        }
    }

    // Method to get the fetched image URL
    public String getImageUrl() {
        return imageUrl;
    }
}