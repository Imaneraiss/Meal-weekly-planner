package com.example.menuplannerapp.ui;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menuplannerapp.R;
import com.example.menuplannerapp.adapter.MenuHistoryAdapter;
import com.example.menuplannerapp.data.database.AppDatabase;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        RecyclerView recyclerView = findViewById(R.id.recyclerHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AppDatabase db = AppDatabase.getInstance(this);
        MenuHistoryAdapter adapter =
                new MenuHistoryAdapter(db.menuDao().getAllMenus());

        recyclerView.setAdapter(adapter);
    }
}
