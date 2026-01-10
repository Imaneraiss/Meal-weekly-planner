package com.example.menuplannerapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.menuplannerapp.R;
import com.example.menuplannerapp.models.MenuItem;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private ArrayList<MenuItem> menuList = new ArrayList<>();

    public void setMenuList(ArrayList<MenuItem> list) {
        menuList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MenuViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItem item = menuList.get(position);

        holder.tvDateTime.setText(item.getDay() + " - " + item.getMeal());
        holder.tvName.setText(item.getRecipe().getName());
        holder.tvCategory.setText("Catégorie: " + item.getRecipe().getCategory());

        Glide.with(holder.itemView.getContext())
                .load(item.getRecipe().getThumbnailUrl())
                .into(holder.ivRecipe);
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    static class MenuViewHolder extends RecyclerView.ViewHolder {

        TextView tvDateTime, tvName, tvCategory;
        ImageView ivRecipe;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDateTime = itemView.findViewById(R.id.tvMealDateTime);
            tvName = itemView.findViewById(R.id.tvRecipeName);
            tvCategory = itemView.findViewById(R.id.tvRecipeCategory);
            ivRecipe = itemView.findViewById(R.id.ivRecipeImage);
        }
    }
}
