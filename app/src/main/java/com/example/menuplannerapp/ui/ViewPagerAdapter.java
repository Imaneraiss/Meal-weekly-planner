package com.example.menuplannerapp.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Retourner le fragment correspondant à la position
        switch (position) {
            case 0:
                return new MenuFragment();
            case 1:
                return new ShoppingFragment();
            default:
                return new MenuFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Nous avons 2 onglets
    }
}