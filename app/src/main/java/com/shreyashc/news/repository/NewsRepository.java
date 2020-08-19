package com.shreyashc.news.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.shreyashc.news.api.NewsApi;
import com.shreyashc.news.api.RetrofitClient;
import com.shreyashc.news.db.ArticleDao;
import com.shreyashc.news.db.ArticleDatabase;
import com.shreyashc.news.models.Article;
import com.shreyashc.news.models.NewsResponse;

import java.util.List;

import retrofit2.Call;

public class NewsRepository {
    private static NewsApi api;
    private static final String TAG = "NewsRepository";

    //    private  static  NewsRepository repository;
    private ArticleDao articleDao;
    public static NewsRepository repository;


    public NewsRepository(Application application) {
        api = RetrofitClient.getClient().create(NewsApi.class);
        ArticleDatabase database = ArticleDatabase.getInstance(application);
        articleDao = database.getArticleDao();

    }

    public static NewsRepository getInstance(Application application) {
        if (repository == null) {
            repository = new NewsRepository(application);

        }
        return repository;
    }

    public Call<NewsResponse> getBreakingNews(String countryCode, int pageNo, String apiKey) {
        return api.getBreakingNews(countryCode, pageNo, apiKey);
    }

    public Call<NewsResponse> searchForNews(String query, int pageNo, String apiKey) {
        return api.searchForNews(query, pageNo, apiKey);
    }
    //saving favs


    public LiveData<List<Article>> getSavedArticles() {
        return articleDao.getAllArticles();
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
