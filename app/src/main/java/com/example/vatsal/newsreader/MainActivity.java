package com.example.vatsal.newsreader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<News> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DownloadList task = new DownloadList();
        task.execute();
    }

    public class DownloadList extends AsyncTask<Void, Void, ArrayList<News>> {
        private ArrayList<News> work() {
            ArrayList<News> list;
            try {
                URL url = new URL("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();
                StringBuilder sb = new StringBuilder();

                while (data != -1) {
                    char current = (char) data;
                    sb.append(current);
                    data = inputStreamReader.read();
                }

                JSONArray jsonArray = new JSONArray(sb.toString());
                Log.i("Array got", "true");
                list = new ArrayList<>();

                while (list.size() < 10) {
                    String number = jsonArray.getString(list.size());

                    String urlString = String.format("https://hacker-news.firebaseio.com/v0/item/%s.json?print=pretty", number);
                    url = new URL(urlString);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();

                    inputStream = connection.getInputStream();
                    inputStreamReader = new InputStreamReader(inputStream);

                    data = inputStreamReader.read();
                    sb = new StringBuilder();

                    while (data != -1) {
                        char current = (char) data;
                        sb.append(current);
                        data = inputStreamReader.read();
                    }
                    JSONObject object = new JSONObject(sb.toString());
                    String JSONurl;
                    try {
                        object.getString("url");
                    } catch (JSONException e) {
                        continue;
                    }
                    list.add(new News(object.getString("title"), object.getString("url")));
                    Log.i("item added", list.size() + "");

                }
                return list;
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("Input Output Error", "Yes");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("JSONException Error", "Yes");
            }
            return null;
        }

        @Override
        protected ArrayList<News> doInBackground(Void... voids) {
            return work();
        }

        @Override
        protected void onPostExecute(ArrayList<News> aVoid) {
            MyAdapter myAdapter = new MyAdapter(aVoid, getApplicationContext());
            recyclerView.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();
        }
    }
}
