package com.shreyashc.news;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.shreyashc.news.repository.NewsRepository;

public class NewsViewModelFactory implements ViewModelProvider.Factory {
    private Application application;
    private NewsRepository newsRepository;

    public NewsViewModelFactory(Application application) {
        this.application = application;
        newsRepository=NewsRepository.getInstance(application);
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new NewsViewModel(application,newsRepository);

    }
}
