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

public class MenuFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView tvEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Création de la vue à partir du XML
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        // Initialiser les vues
        recyclerView = view.findViewById(R.id.recyclerViewMenu);
        tvEmpty = view.findViewById(R.id.tvEmptyMenu);

        // 1. Crée un nouveau LinearLayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        // 2. LinearLayoutManager configure :
        //    - Orientation : vertical (par défaut)
        //    - Sens de défilement : normal
        //    - Comportement de recyclage

        // 3. Applique au RecyclerView
        recyclerView.setLayoutManager(layoutManager);
        // Résultat : RecyclerView sait COMMENT afficher les items


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

    // !!!!! cacher le message si vous ajoutez les donnees
    private void hideEmptyMessage() {
        recyclerView.setVisibility(View.VISIBLE);
        tvEmpty.setVisibility(View.GONE);
    }
}