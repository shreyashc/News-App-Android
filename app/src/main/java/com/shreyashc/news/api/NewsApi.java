package com.shreyashc.news.api;

import com.shreyashc.news.models.NewsResponse;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApi {

    /*@GET("v2/top-headlines")
    Call<Response<NewsResponse>> getBreakingNews(
            @Query("county") String country,
            @Query("page") int pageNumber,
            @Query("apiKey") String apiKey);
*/
    @GET("v2/top-headlines")
    Call<NewsResponse> getBreakingNews(
            @Query("country") String country,
            @Query("page") int pageNumber,
            @Query("apiKey") String apiKey);

    @GET("v2/everything")
    Call<Response<NewsResponse>> searchForNews(
            @Query("q") String searchQuery,
            @Query("page") int pageNumber,
            @Query("apiKey") String apiKey);

}
