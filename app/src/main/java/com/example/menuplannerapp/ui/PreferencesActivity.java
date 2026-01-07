package com.example.menuplannerapp.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.example.menuplannerapp.R;

public class PreferencesActivity extends AppCompatActivity {

    private CheckBox cbVegetarian, cbGlutenFree, cbHalal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        cbVegetarian = findViewById(R.id.cbVegetarian);
        cbGlutenFree = findViewById(R.id.cbGlutenFree);
        cbHalal = findViewById(R.id.cbHalal);
        Button btnSave = findViewById(R.id.btnSavePreferences);

        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);

        cbVegetarian.setChecked(prefs.getBoolean("vegetarian", false));
        cbGlutenFree.setChecked(prefs.getBoolean("gluten_free", false));
        cbHalal.setChecked(prefs.getBoolean("halal", false));

        btnSave.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("vegetarian", cbVegetarian.isChecked());
            editor.putBoolean("gluten_free", cbGlutenFree.isChecked());
            editor.putBoolean("halal", cbHalal.isChecked());
            editor.apply();
            finish();
        });
    }
}