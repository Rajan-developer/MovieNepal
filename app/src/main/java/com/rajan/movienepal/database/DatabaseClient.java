package com.rajan.movienepal.database;

import android.content.Context;

import androidx.room.Room;

import com.rajan.movienepal.R;

public class DatabaseClient {

    private Context mContext;
    private static DatabaseClient mInstance;

    //our app database object
    private AppDatabase appDatabase;

    private DatabaseClient(Context context) {
        this.mContext = context;

        //creating the app databse with Room databse builder
        //Test is the name of the database
        appDatabase = Room.databaseBuilder(mContext, AppDatabase.class, "MovieNepal").build();
    }

    public static synchronized DatabaseClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(context);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
