package com.example.menuplannerapp.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.menuplannerapp.data.entity.MenuEntity;

import java.util.List;

@Dao
public interface MenuDao {

    @Insert
    void insertMenu(MenuEntity menu);

    @Query("SELECT * FROM menus ORDER BY date DESC")
    List<MenuEntity> getAllMenus();

    @Query("DELETE FROM menus")
    void deleteAll();
}