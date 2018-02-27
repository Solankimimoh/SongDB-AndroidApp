package com.songdb.webmob.songsdb.app;

import android.app.Application;

import com.songdb.webmob.songsdb.db.DBHelper;

public class SqliteApp extends Application {

    private DBHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        dbHelper = new DBHelper(this);
    }


    public DBHelper getDbHelper() {
        return dbHelper;
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
