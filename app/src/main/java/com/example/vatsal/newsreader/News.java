package com.example.vatsal.newsreader;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by vatsal on 25/3/18.
 */

public class News {
    static Context context;
    static SQLiteDatabase db;
    String title;
    int id;

    News(String title, String url, int id) {
        this.title = title.substring(0, Math.min(title.length(), 50));
        this.id = id;
        db.execSQL(String.format("insert into news (url, id) values ('%s', %d )", url, id));
    }

    static void initialise(SQLiteDatabase database) {
        db = database;
        db.execSQL("create table if not exists news (url varchar, id integer primary key)");
    }

    static String getUrl(int id) {
        Cursor c = db.rawQuery(String.format("select url from news where id = %d", id), null);
        int urlIndex = c.getColumnIndex("url");
        c.moveToFirst();
        return c.getString(urlIndex);
    }
}
