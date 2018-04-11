package com.example.vatsal.newsreader;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NewsInstance {
    @GET("item/{id}.json?print=pretty")
    Call<NewsAPI> getNewsInstance(@Path("id") int id);
}
