package com.rajan.movienepal.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.rajan.movienepal.database.DAO.MovieDAO;
import com.rajan.movienepal.database.entity.Movie;

@Database(entities = {Movie.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MovieDAO movieDAO();
}
