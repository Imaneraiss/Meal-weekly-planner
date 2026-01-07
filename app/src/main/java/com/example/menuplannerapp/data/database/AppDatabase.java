package com.example.menuplannerapp.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.menuplannerapp.data.dao.FavoriteRecipeDao;
import com.example.menuplannerapp.data.dao.MenuDao;
import com.example.menuplannerapp.data.entity.FavoriteRecipeEntity;
import com.example.menuplannerapp.data.entity.MenuEntity;

@Database(
        entities = {MenuEntity.class, FavoriteRecipeEntity.class},
        version = 1
)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract MenuDao menuDao();
    public abstract FavoriteRecipeDao favoriteRecipeDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "meal_planner_db"
            ).allowMainThreadQueries().build();
        }
        return instance;
    }
}
