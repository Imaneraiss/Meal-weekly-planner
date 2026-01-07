package com.example.menuplannerapp.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.menuplannerapp.data.entity.FavoriteRecipeEntity;

import java.util.List;

@Dao
public interface FavoriteRecipeDao {

    @Insert
    void insertFavorite(FavoriteRecipeEntity recipe);

    @Query("SELECT * FROM favorites")
    List<FavoriteRecipeEntity> getFavorites();
}
