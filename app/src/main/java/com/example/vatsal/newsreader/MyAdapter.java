package com.example.vatsal.newsreader;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
/**
 * Created by vatsal on 25/3/18.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<News> dataSet;
    private Context context;

    public MyAdapter(ArrayList<News> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_view, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        String name = dataSet.get(position).title;
        holder.textView.setText(name);
        holder.textView.setOnClickListener((View view) -> {
            Intent intent = new Intent(context, webPage.class);
            intent.putExtra("id", dataSet.get(position).id);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            textView = (TextView) view.findViewById(R.id.list_item_View);
        }
    }
}
