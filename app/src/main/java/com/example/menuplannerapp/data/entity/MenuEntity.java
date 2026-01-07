package com.example.menuplannerapp.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "menus")
public class MenuEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String date;
    public String breakfast;
    public String lunch;
    public String dinner;
}
