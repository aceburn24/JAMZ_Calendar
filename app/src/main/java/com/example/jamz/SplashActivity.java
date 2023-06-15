package com.example.jamz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    private int currentIndex;
    private static final int[] SPLASH_TIMERS = {2000, 1500, 1500, 1500};
    private static final int[] LAYOUT_RESOURCES = {
            R.layout.activity_splash,
            R.layout.activity_second_splash,
            R.layout.activity_third_splash,
            R.layout.activity_fourth_splash
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the current splash screen index, default to 0 if it doesn't exist
        currentIndex = getIntent().getIntExtra("currentIndex", 0);

        // Set the layout for the current splash screen
        setContentView(LAYOUT_RESOURCES[currentIndex]);

        int timer = SPLASH_TIMERS[currentIndex];

        new Handler().postDelayed(() -> {
            // If not the last splash screen, start the next one
            Intent intent;
            if (currentIndex + 1 < LAYOUT_RESOURCES.length) {
                intent = new Intent(SplashActivity.this, SplashActivity.class);
                intent.putExtra("currentIndex", currentIndex + 1);
            } else {
                // If the last splash screen, start the main activity
                intent = new Intent(SplashActivity.this, MainActivity.class);
            }
            startActivity(intent);
            finish();
        }, timer);
    }
}