package com.example.menuplannerapp.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private final MenuFragment menuFragment = new MenuFragment();
    private final ShoppingFragment shoppingFragment = new ShoppingFragment();

    public ViewPagerAdapter(@NonNull FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) return menuFragment;
        else return shoppingFragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    // Ajout de getters pour accéder aux fragments
    public MenuFragment getMenuFragment() {
        return menuFragment;
    }

    public ShoppingFragment getShoppingFragment() {
        return shoppingFragment;
    }
}
