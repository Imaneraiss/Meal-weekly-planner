package com.example.menuplannerapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menuplannerapp.R;
import com.example.menuplannerapp.models.ShoppingItem;

import java.util.ArrayList;

public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ShoppingViewHolder> {

    private ArrayList<ShoppingItem> shoppingList = new ArrayList<>();

    public void setShoppingList(ArrayList<ShoppingItem> list) {
        shoppingList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ShoppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShoppingViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shopping, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingViewHolder holder, int position) {
        ShoppingItem item = shoppingList.get(position);

        holder.checkBox.setChecked(item.isPurchased());
        holder.tvIngredient.setText(item.getIngredient());
        holder.tvQuantity.setText(item.getTotalQuantity());

    }

    @Override
    public int getItemCount() {
        return shoppingList.size();
    }

    static class ShoppingViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        TextView tvIngredient, tvQuantity;

        public ShoppingViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.cbPurchased);
            tvIngredient = itemView.findViewById(R.id.tvIngredientName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
        }
    }
}
