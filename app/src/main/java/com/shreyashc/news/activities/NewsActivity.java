package com.shreyashc.news.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.shreyashc.news.R;
import com.shreyashc.news.viewmodels.NewsViewModel;
import com.shreyashc.news.viewmodels.NewsViewModelFactory;

public class NewsActivity extends AppCompatActivity {

    private static NewsViewModel newsViewModel;

    public static NewsViewModel getNewsViewModel() {
        return newsViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.newsNavHostFragment);
        newsViewModel = new ViewModelProvider(this, new NewsViewModelFactory(getApplication())).get(NewsViewModel.class);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

}