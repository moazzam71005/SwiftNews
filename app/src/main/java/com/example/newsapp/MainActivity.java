package com.example.newsapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;


interface TaskCompletedListener{    //implement interface to ensure that rest of code runs after async task has finished
    void onTaskComplete(Document doc);
}



public class MainActivity extends AppCompatActivity implements CategoryRVAdaptor.CategoryClickInterface, TaskCompletedListener{
    private RecyclerView newsRV, categoryRV;
    public static ArrayList<Articles> worldArticles = new ArrayList<>();
    public static ArrayList<Articles> pakistanArticles = new ArrayList<>();
    public static ArrayList<Articles> healthArticles = new ArrayList<>();
    public static ArrayList<Articles> religionArticles = new ArrayList<>();
    public static ArrayList<Articles> sportsArticles = new ArrayList<>();
    public static ArrayList<Articles> scienceArticles = new ArrayList<>();
    public static ArrayList<Articles> technologyArticles = new ArrayList<>();
    public static ArrayList<Articles> politicsArticles = new ArrayList<>();

    //upcasting
    Category[] categories = {new PakistanNewsCategory(this), new WorldNewsCategory(this), new ReligionNewsCategory(this), new HealthNewsCategory(this), new TechnologyNewsCategory(this), new ScienceNewsCategory(this), new SportsNewsCategory(this), new PoliticsNewsCategory(this)};

    private ProgressBar loadingPB;
    public static ArrayList<Articles> articlesArrayList;
    private ArrayList<CategoryRVModel> categoryRVModelArrayList;
    private CategoryRVAdaptor categoryRVAdaptor;
    private NewsRVAdaptor newsRVAdaptor;
    private TextView selectedCategoryTV;
    Switch switchMode;
    boolean nightMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private static final String TAG = "MainActivity";
    private static final String PREFS_NAME = "MODE";
    private static final String NIGHT_MODE_KEY = "night";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean(NIGHT_MODE_KEY, false);
        Log.d(TAG, "Night mode from prefs: " + nightMode);
        AppCompatDelegate.setDefaultNightMode(nightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        switchMode = findViewById(R.id.switchMode);
        // Set the initial state of the switch based on the current night mode state
        switchMode.setChecked(nightMode);
        Log.d(TAG, "Initial switch state: " + nightMode);

        // Set an OnCheckedChangeListener to handle switch state changes
        switchMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "Switch state changed: " + isChecked);
                // Update the app's night mode based on the switch state
                AppCompatDelegate.setDefaultNightMode(isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

                // Save the new night mode state in SharedPreferences
                sharedPreferences.edit().putBoolean(NIGHT_MODE_KEY, isChecked).apply();
                Log.d(TAG, "Night mode saved to prefs: " + isChecked);
            }
        });


        selectedCategoryTV = findViewById(R.id.idTVSelectedCategory);

        newsRV = findViewById(R.id.idRVNews);
        categoryRV = findViewById(R.id.idRVCategories);
        loadingPB = findViewById(R.id.idPBLoading);
        articlesArrayList = new ArrayList<>();
        categoryRVModelArrayList = new ArrayList<>();
        newsRVAdaptor = new NewsRVAdaptor(articlesArrayList, this);
        categoryRVAdaptor = new CategoryRVAdaptor(categoryRVModelArrayList, this, this::onCategoryClick);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsRVAdaptor);
        categoryRV.setAdapter(categoryRVAdaptor);
        try {
            getCategories();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //System.out.print("world news ");
        getNews("World");




//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }

    private boolean isNightModeEnabled() {
        // Get the current night mode state from SharedPreferences
        return sharedPreferences.getBoolean("night", false);
    }

    public void displayNews(){
        newsRVAdaptor.notifyDataSetChanged();
        loadingPB.setVisibility(View.GONE);
    }

    private void getCategories() throws IOException {
        categoryRVModelArrayList.add(new CategoryRVModel("World", "https://eos.org/wp-content/uploads/2019/12/earth-at-night-from-space.jpg"));
        categoryRVModelArrayList.add(new CategoryRVModel("Pakistan", "https://i.pinimg.com/564x/52/85/e6/5285e6f9e5383574388343bdb6b1d69c.jpg"));
        categoryRVModelArrayList.add(new CategoryRVModel("Religion", "https://cdn.pixabay.com/photo/2019/05/04/18/13/quran-4178711_640.jpg"));
        categoryRVModelArrayList.add(new CategoryRVModel("Health", "https://i.pinimg.com/564x/b0/f9/9e/b0f99e72847d92adf7f790c1a7e972f2.jpg"));
        categoryRVModelArrayList.add(new CategoryRVModel("Technology", "https://miro.medium.com/v2/resize:fit:1156/1*VzexncB2H2chkgC87wcnAA.jpeg"));
        categoryRVModelArrayList.add(new CategoryRVModel("Science", "https://images.unsplash.com/photo-1576319155264-99536e0be1ee?q=80&w=1000&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8cXVhbnR1bSUyMHBoeXNpY3N8ZW58MHx8MHx8fDA%3D"));
        categoryRVModelArrayList.add(new CategoryRVModel("Sports", "https://media.istockphoto.com/id/942206186/photo/sports-balls-on-the-field-with-yard-line-soccer-ball-american-football-and-baseball-in-yellow.webp?b=1&s=170667a&w=0&k=20&c=Y1mLfW31gtI9IdbgqbRfl59Fxdvv6eObgQ3Iw5rCc-c="));
        categoryRVModelArrayList.add(new CategoryRVModel("Politics", "https://pics.craiyon.com/2023-10-22/9ef6f2f74d10408c8ae5d0c1dfbe4a48.webp"));



        categoryRVAdaptor.notifyDataSetChanged();
    }

    private void getNews(String category){
        selectedCategoryTV.setText(category);
        loadingPB.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
        if(category.equals("Pakistan")) {
            if (pakistanArticles.isEmpty()) {
                categories[0].execute();
            } else {
                articlesArrayList.addAll(pakistanArticles);
                displayNews();
            }

        }
        else if(category.equals("World")) {
            if(worldArticles.isEmpty()){
                categories[1].execute();
                //new WorldNewsCategory(this).execute();
            }else{
                articlesArrayList.addAll(worldArticles);
                displayNews();
            }

        }

        else if(category.equals("Religion")) {
            if(religionArticles.isEmpty()){
                categories[2].execute();
                //new ReligionNewsCategory(this).execute();
            }else{
                articlesArrayList.addAll(religionArticles);
                displayNews();
            }

        }
        else if(category.equals("Health")){
            if(healthArticles.isEmpty()){
                categories[3].execute();
                //new HealthNewsCategory(this).execute();
            }else{
                articlesArrayList.addAll(healthArticles);
                displayNews();
            }
        }
        else if(category.equals("Technology")){
            if(technologyArticles.isEmpty()){
                categories[4].execute();
                //new TechnologyNewsCategory(this).execute();
            }else{
                articlesArrayList.addAll(technologyArticles);
                displayNews();
            }
        }
        else if(category.equals("Science")){
            if(scienceArticles.isEmpty()){
                categories[5].execute();
                //new ScienceNewsCategory(this).execute();
            }else{
                articlesArrayList.addAll(scienceArticles);
                displayNews();
            }
        }
        else if(category.equals("Sports")){
            if(sportsArticles.isEmpty()){
                categories[6].execute();
                //new SportsNewsCategory(this).execute();
            }else{
                articlesArrayList.addAll(sportsArticles);
                displayNews();
            }
        }
        else if(category.equals("Politics")){
            if(politicsArticles.isEmpty()){
                categories[7].execute();
                //new PoliticsNewsCategory(this).execute();
            }else{
                articlesArrayList.addAll(politicsArticles);
                displayNews();
            }
        }

    }

    @Override
    public void onCategoryClick(int position) {
        String category = categoryRVModelArrayList.get(position).getCategory();
        getNews(category);

    }


    @Override
    public void onTaskComplete(Document doc){
        //System.out.println("Hello");
        newsRVAdaptor.notifyDataSetChanged();
        loadingPB.setVisibility(View.GONE);
    }
}
