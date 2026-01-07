package com.example.menuplannerapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.menuplannerapp.R;

public class ShoppingFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView tvEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflater le layout
        View view = inflater.inflate(R.layout.fragment_shopping, container, false);

        // Initialiser les vues
        recyclerView = view.findViewById(R.id.recyclerViewShopping);
        tvEmpty = view.findViewById(R.id.tvEmptyShopping);

        // Configurer le RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Pour l'instant, on affiche le message "vide"
        // A ajouter l'adapter et les données ici
        showEmptyMessage();

        return view;
    }

    // Méthode pour afficher le message si la liste est vide
    private void showEmptyMessage() {
        recyclerView.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.VISIBLE);
    }

    // Méthode pour cacher le message
    private void hideEmptyMessage() {
        recyclerView.setVisibility(View.VISIBLE);
        tvEmpty.setVisibility(View.GONE);
    }
}