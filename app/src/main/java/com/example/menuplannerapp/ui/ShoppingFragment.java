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
import com.example.menuplannerapp.models.ShoppingItem;

import java.util.ArrayList;

public class ShoppingFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView tvEmpty;
    private ShoppingAdapter adapter;

    // Liste sauvegardée pour quand le fragment n'est pas encore créé
    private ArrayList<ShoppingItem> pendingList = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping, container, false);

        // Initialiser les vues
        recyclerView = view.findViewById(R.id.recyclerViewShopping);
        tvEmpty = view.findViewById(R.id.tvEmptyShopping);

        // Configurer le RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Afficher le message vide par défaut
        showEmptyMessage();

        // Si des données sont en attente, les appliquer maintenant
        if (pendingList != null) {
            updateList(pendingList);
            pendingList = null;
        }

        return view;
    }

    /**
     * Mise à jour de la liste de courses
     * PROTECTION : Vérifie que la vue existe avant modification
     */
    public void updateList(ArrayList<ShoppingItem> list) {
        // Si la vue n'existe pas encore, sauvegarder pour plus tard
        if (recyclerView == null || tvEmpty == null) {
            pendingList = list;
            return;
        }

        if (list != null && !list.isEmpty()) {
            hideEmptyMessage();
            if (adapter == null) {
                adapter = new ShoppingAdapter();
            }
            adapter.setShoppingList(list);
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
}