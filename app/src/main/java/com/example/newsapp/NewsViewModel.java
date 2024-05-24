// NewsViewModel.java
package com.example.newsapp;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class NewsViewModel extends AndroidViewModel {
    private MutableLiveData<List<Articles>> newsLiveData;

    public NewsViewModel(Application application) {
        super(application);
        newsLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Articles>> getNewsLiveData() {
        return newsLiveData;
    }

    public void loadNews() {
        // Simulate loading news data in a background thread
        new Thread(() -> {
            List<Articles> articlesList = fetchNews();
            newsLiveData.postValue(articlesList);
        }).start();
    }

    private List<Articles> fetchNews() {
        // Replace this with actual data fetching logic
        // For example, an API call to get news articles
        List<Articles> articlesList = new ArrayList<>();
        // Populate the list with dummy data or fetched data
        return articlesList;
    }
}
