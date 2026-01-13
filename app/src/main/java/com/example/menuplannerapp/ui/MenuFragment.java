package com.example.menuplannerapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.menuplannerapp.R;
import com.example.menuplannerapp.models.MenuItem;

import java.util.ArrayList;

public class MenuFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView tvEmpty;
    private MenuAdapter adapter;

    // Menu sauvegardé pour quand le fragment n'est pas encore créé
    private ArrayList<MenuItem> pendingMenu = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        // Initialiser les vues
        recyclerView = view.findViewById(R.id.recyclerViewMenu);
        tvEmpty = view.findViewById(R.id.tvEmptyMenu);

        // Configurer le RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // Afficher le message vide par défaut
        showEmptyMessage();

        // Si des données sont en attente, les appliquer maintenant
        if (pendingMenu != null) {
            updateMenu(pendingMenu);
            pendingMenu = null;
        }

        return view;
    }

    /**
     * Mise à jour du menu
     * PROTECTION : Vérifie que la vue existe avant modification
     */
    public void updateMenu(ArrayList<MenuItem> menu) {
        // Si la vue n'existe pas encore, sauvegarder pour plus tard
        if (recyclerView == null || tvEmpty == null) {
            pendingMenu = menu;
            return;
        }

        if (menu != null && !menu.isEmpty()) {
            hideEmptyMessage();
            if (adapter == null) {
                adapter = new MenuAdapter();
            }
            adapter.setMenuList(menu);
            recyclerView.setAdapter(adapter);
        } else {
            showEmptyMessage();
        }
    }

    private void showEmptyMessage() {
        if (recyclerView != null && tvEmpty != null) {
            recyclerView.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void hideEmptyMessage() {
        if (recyclerView != null && tvEmpty != null) {
            recyclerView.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnPreferences = view.findViewById(R.id.btnPreferences);
        btnPreferences.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), PreferencesActivity.class));
        });

        Button btnHistory = view.findViewById(R.id.btnHistory);
        btnHistory.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), HistoryActivity.class));
        });
    }
}