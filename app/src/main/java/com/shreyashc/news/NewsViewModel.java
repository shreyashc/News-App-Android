package com.shreyashc.news;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.shreyashc.news.models.Article;
import com.shreyashc.news.models.NewsResponse;
import com.shreyashc.news.repository.NewsRepository;
import com.shreyashc.news.util.Resource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.net.NetworkCapabilities.TRANSPORT_CELLULAR;
import static android.net.NetworkCapabilities.TRANSPORT_ETHERNET;
import static android.net.NetworkCapabilities.TRANSPORT_WIFI;

public class NewsViewModel extends AndroidViewModel {
    public static final String API_KEY = "cdc46dec344149d4b3411a8a4e17bdf7";
    private static final String TAG = "NewsViewModel";
    Application app;
    private NewsRepository newsRepository;
    private MutableLiveData<NewsResponse> mutableLiveData;

    public NewsViewModel(@NonNull Application application, NewsRepository newsRepository) {
        super(application);
        this.app = application;
        this.newsRepository = newsRepository;
    }

    public void init() {
        if (mutableLiveData != null) {
            return;
        }
        ///changing getApplication() to app
        newsRepository = NewsRepository.getInstance(app);
    }

    public MutableLiveData<NewsResponse> getNews() {
        if(isConnected()) {
            return  newsRepository.getBreakingNews("us", 1, API_KEY);

        }else {
            return  null;
        }


    }

    public void saveArticle(Article article){
        newsRepository.upsert(article);
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network activeNetwork = connectivityManager.getActiveNetwork();
            if (activeNetwork == null) return false;
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
            if (capabilities == null) return false;

            if (capabilities.hasTransport(TRANSPORT_WIFI)) return true;
            if (capabilities.hasTransport(TRANSPORT_CELLULAR)) return true;
            if (capabilities.hasTransport(TRANSPORT_ETHERNET)) return true;
            else return false;

        } else {

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo == null) return false;
            if (networkInfo.isConnectedOrConnecting()) return true;
            int type = networkInfo.getType();
            if (type == ConnectivityManager.TYPE_WIFI) return true;
            if (type == ConnectivityManager.TYPE_MOBILE) return true;
            if (type == ConnectivityManager.TYPE_ETHERNET) return true;

        }
        return false;
    }


}









