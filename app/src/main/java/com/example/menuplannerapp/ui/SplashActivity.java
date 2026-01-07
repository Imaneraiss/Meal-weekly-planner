package com.example.menuplannerapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.example.menuplannerapp.R;

public class SplashActivity extends AppCompatActivity {

    // Durée d'affichage en millisecondes (3 secondes)
    private static final int SPLASH_DURATION = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Cacher la barre d'action
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Attendre 3 secondes puis lancer MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Lancer l'activité principale
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);

                // Fermer le splash screen
                finish();
            }
        }, SPLASH_DURATION);
    }
}