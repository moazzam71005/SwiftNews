package com.example.newsapp;

import android.os.AsyncTask;

import org.jsoup.nodes.Document;

import java.util.ArrayList;

abstract class Category extends AsyncTask<String, Void, Document>  {    //implement abstraction
    Document doc;
    @Override
    protected abstract Document doInBackground(String... urls);
    //return null;

    @Override
    protected abstract void onPostExecute(Document doc);

    protected void listener(ArrayList<Articles> articles, TaskCompletedListener listener, Document doc){
        //if(MainActivity.articlesArrayList.size() == 10){
            articles.clear();
            articles.addAll(MainActivity.articlesArrayList);
            listener.onTaskComplete(doc);
        //}
    }
}
