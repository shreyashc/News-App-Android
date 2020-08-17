package com.shreyashc.news.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.shreyashc.news.api.NewsApi;
import com.shreyashc.news.api.RetrofitClient;
import com.shreyashc.news.db.ArticleDao;
import com.shreyashc.news.db.ArticleDatabase;
import com.shreyashc.news.models.Article;
import com.shreyashc.news.models.NewsResponse;
import com.shreyashc.news.util.Resource;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRepository {
    private static NewsApi api;
    private static final String TAG = "NewsRepository";

    //    private  static  NewsRepository repository;
    private ArticleDao articleDao;
    private LiveData<List<Article>> savedArticles;
    public static NewsRepository repository;


    public NewsRepository(Application application) {
        api = RetrofitClient.getClient().create(NewsApi.class);
        ArticleDatabase database = ArticleDatabase.getInstance(application);
        articleDao = database.getArticleDao();
        savedArticles = articleDao.getAllArticles();
    }

    public static NewsRepository getInstance(Application application){
        if(repository==null){
            repository = new NewsRepository(application);

        }
        return  repository;
    }

    public MutableLiveData<NewsResponse> getBreakingNews(String countryCode,int pageNo, String apiKey){
         final MutableLiveData<NewsResponse> newsList = new MutableLiveData<>();
            api.getBreakingNews(countryCode,pageNo,apiKey).enqueue(new Callback<NewsResponse>() {
                @Override
                public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                    newsList.postValue(response.body());
                }

                @Override
                public void onFailure(Call<NewsResponse> call, Throwable t) {
                    Log.d(TAG, "onFailure: "+t.getMessage());
                    newsList.setValue(null);
                }
            });
        return newsList;
    }








    //saving favs


    public LiveData<List<Article>> getSavedArticles() {
        return savedArticles;
    }


    public void upsert(Article article) {
        new UpsertArticleAsyncTask(articleDao).execute(article);
    }

    public void deleteArticle(Article article) {
        new DeleteArticleAsyncTask(articleDao).execute(article);
    }



   







    //AsyncTasks
    private static class UpsertArticleAsyncTask extends AsyncTask<Article, Void, Void> {

        private ArticleDao articleDao;

        public UpsertArticleAsyncTask(ArticleDao articleDao) {
            this.articleDao = articleDao;
        }

        @Override
        protected Void doInBackground(Article... articles) {
            articleDao.upsert(articles[0]);
            return null;
        }
    }

    private static class DeleteArticleAsyncTask extends AsyncTask<Article, Void, Void> {

        private ArticleDao articleDao;

        public DeleteArticleAsyncTask(ArticleDao articleDao) {
            this.articleDao = articleDao;
        }

        @Override
        protected Void doInBackground(Article... articles) {
            articleDao.deleteArticle(articles[0]);
            return null;
        }
    }


}
