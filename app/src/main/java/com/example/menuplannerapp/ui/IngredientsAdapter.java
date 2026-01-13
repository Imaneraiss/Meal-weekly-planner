package com.example.menuplannerapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menuplannerapp.R;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private final List<String> ingredientsList;

    public IngredientsAdapter(List<String> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String ingredient = ingredientsList.get(position);
        holder.tvIngredient.setText("• " + ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredientsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvIngredient;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIngredient = itemView.findViewById(R.id.tvIngredientItem);
        }
    }
}