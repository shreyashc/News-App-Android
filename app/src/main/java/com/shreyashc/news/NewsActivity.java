package com.shreyashc.news;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        BottomNavigationView bottomNavigationView =findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.newsNavHostFragment);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

}