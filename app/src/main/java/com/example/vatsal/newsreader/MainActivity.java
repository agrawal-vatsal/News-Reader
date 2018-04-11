package com.example.vatsal.newsreader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public static final int NewsNumber = 50;
    static final String BASE_URL = "https://hacker-news.firebaseio.com/v0/";
    ArrayList<News> newsList;
    RecyclerView recyclerView;
    List<Integer> list;
    SQLiteDatabase database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = this.openOrCreateDatabase("Data", MODE_PRIVATE, null);
        News.initialise(database);
        database.execSQL("Delete from news");
        recyclerView = findViewById(R.id.recyclerView);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Task task = new Task();
        task.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.execSQL("delete from news");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        onDestroy();
        startActivity(intent);
    }

    public class Task extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();
            IDList idList = retrofit.create(IDList.class);
            Call<List<Integer>> ids = idList.getIDs();
            try {
                list = ids.execute().body();
            } catch (IOException e) {
                return null;
            }
            NewsInstance newsInstance = retrofit.create(NewsInstance.class);
            int i = 0;
            newsList = new ArrayList<>();
            while (newsList.size() < NewsNumber && i < list.size()) {
                Call<NewsAPI> news = newsInstance.getNewsInstance(list.get(i));
                i++;
                NewsAPI api;
                try {
                    api = news.execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
                if (api.getUrl() != null) {
                    newsList.add(new News(api.getTitle(), api.getUrl(), api.getId()));
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            recyclerView.setAdapter(new MyAdapter(newsList, getApplicationContext()));
            progressDialog.hide();
        }


    }
}

