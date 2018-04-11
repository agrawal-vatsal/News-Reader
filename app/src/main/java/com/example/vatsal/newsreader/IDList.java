package com.example.vatsal.newsreader;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IDList {
    @GET("topstories.json?print=pretty")
    Call<List<Integer>> getIDs();
}
