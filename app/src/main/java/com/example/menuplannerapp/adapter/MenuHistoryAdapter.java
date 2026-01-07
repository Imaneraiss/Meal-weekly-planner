package com.example.menuplannerapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menuplannerapp.R;
import com.example.menuplannerapp.data.entity.MenuEntity;

import java.util.List;

public class MenuHistoryAdapter
        extends RecyclerView.Adapter<MenuHistoryAdapter.ViewHolder> {

    private final List<MenuEntity> menuList;

    public MenuHistoryAdapter(List<MenuEntity> menuList) {
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuEntity menu = menuList.get(position);
        holder.tvDate.setText(menu.date);
        holder.tvMeals.setText(
                "Breakfast: " + menu.breakfast +
                        "\nLunch: " + menu.lunch +
                        "\nDinner: " + menu.dinner
        );
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate, tvMeals;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvMeals = itemView.findViewById(R.id.tvMeals);
        }
    }
}
