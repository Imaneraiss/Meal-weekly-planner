package com.example.menuplannerapp.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorites")
public class FavoriteRecipeEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String recipeId;
    public String recipeName;
}