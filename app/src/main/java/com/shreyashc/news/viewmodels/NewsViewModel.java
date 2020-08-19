package com.shreyashc.news.viewmodels;

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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.net.NetworkCapabilities.TRANSPORT_CELLULAR;
import static android.net.NetworkCapabilities.TRANSPORT_ETHERNET;
import static android.net.NetworkCapabilities.TRANSPORT_WIFI;

public class NewsViewModel extends AndroidViewModel {

    //    public static final String API_KEY = "cdc46dec344149d4b3411a8a4e17bdf7"; //apikey1
    public static final String API_KEY = "7469121d99de41a7a8efe2c20a5e72f2";       //apikey2
    private static final String TAG = "NewsViewModel";
    public MutableLiveData<Resource<NewsResponse>> breakingNews = new MutableLiveData();
    public NewsResponse breakingNewsResponse = null;
    public MutableLiveData<Resource<NewsResponse>> searchNews = new MutableLiveData<>();
    public NewsResponse searchNewsResponse = null;
    Application app;
    //search news...
    String oldQuery = null;
    private NewsRepository newsRepository;
    private int BreakingNewsPageNo = 1;
    private int searchNewsPage = 1;

    public NewsViewModel(@NonNull Application application, NewsRepository newsRepository) {
        super(application);
        this.app = application;
        this.newsRepository = newsRepository;
    }

    public int getBreakingNewsPageNo() {
        return BreakingNewsPageNo;
    }

    public int getSearchNewsPage() {
        return searchNewsPage;
    }

    public void init() {
        newsRepository = NewsRepository.getInstance(app);
        safeBreakingNewsCall();
    }

    //saved articles........
    public void saveArticle(Article article) {
        newsRepository.upsert(article);
    }

    public LiveData<List<Article>> getSavedArticles() {
        return newsRepository.getSavedArticles();
    }

    public void deleteArticle(Article article) {
        newsRepository.deleteArticle(article);
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
            return capabilities.hasTransport(TRANSPORT_ETHERNET);

        } else {

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo == null) return false;
            if (networkInfo.isConnectedOrConnecting()) return true;
            int type = networkInfo.getType();
            if (type == ConnectivityManager.TYPE_WIFI) return true;
            if (type == ConnectivityManager.TYPE_MOBILE) return true;
            return type == ConnectivityManager.TYPE_ETHERNET;

        }
    }

    ///breaking News......
    public void safeBreakingNewsCall() {
        breakingNews.postValue(Resource.loading(new NewsResponse()));

        if (isConnected()) {
            Call<NewsResponse> call = newsRepository.getBreakingNews("in", BreakingNewsPageNo, API_KEY);
            call.enqueue(new Callback<NewsResponse>() {
                @Override
                public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                    if (response.body() == null) return;
                    BreakingNewsPageNo++;
                    if (breakingNewsResponse == null) {
                        breakingNewsResponse = response.body();
                    } else {
                        List<Article> newArticles = response.body().getArticles();
                        if (breakingNewsResponse.getArticles() != null) {
                            breakingNewsResponse.getArticles().addAll(newArticles);
                        }
                    }
                    breakingNews.postValue(Resource.success(breakingNewsResponse));
                }

                @Override
                public void onFailure(Call<NewsResponse> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                    breakingNews.postValue(Resource.error("Failed to load News"));
                }
            });
        } else {
            breakingNews.postValue(Resource.error("No internet connection"));
        }


    }

    public void searchForNews(String query) {
        searchNews.postValue(Resource.loading(new NewsResponse()));
        final boolean sameSearch;
        if (isConnected()) {
            //caching old query and ckecking if same search
            if (oldQuery == null) {
                oldQuery = query;
                sameSearch = false;
                searchNewsPage = 1;
            } else {

                if (oldQuery.equals(query)) {
                    sameSearch = true;
                } else {
                    sameSearch = false;
                    oldQuery = null;
                    searchNewsPage = 1;
                }
            }

            //requesting call obj...
            Call<NewsResponse> call = newsRepository.searchForNews(query, searchNewsPage, API_KEY);

            call.enqueue(new Callback<NewsResponse>() {
                @Override
                public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                    if (response.body() == null) return;
                    searchNewsPage++;
                    if (searchNewsResponse == null) {
                        searchNewsResponse = response.body();
                    } else {

                        List<Article> newArticles = response.body().getArticles();

                        if (sameSearch) {
                            if (searchNewsResponse.getArticles() != null) {
                                searchNewsResponse.getArticles().addAll(newArticles);
                            }

                        } else {
                            //removing all old articles of different query
                            searchNewsResponse = null;
                            searchNewsResponse = response.body();
                        }
                    }

                    searchNews.postValue(Resource.success(searchNewsResponse));
                }

                @Override
                public void onFailure(Call<NewsResponse> call, Throwable t) {
                    searchNews.postValue(Resource.error("Failed to load News"));
                }
            });
        } else {
            searchNews.postValue(Resource.error("No internet connection"));
        }

    }

}









