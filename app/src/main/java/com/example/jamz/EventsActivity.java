package com.example.jamz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class EventsActivity extends AppCompatActivity {


    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home;
    LinearLayout notes;
    LinearLayout events;
    LinearLayout settings;
    LinearLayout about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home = findViewById(R.id.home);
        notes = findViewById(R.id.notes);
        events = findViewById(R.id.events);
        settings = findViewById(R.id.settings);
        about = findViewById(R.id.about);

        menu.setOnClickListener(
                v -> openDrawer(drawerLayout));
        home.setOnClickListener(v -> redirectActivity(EventsActivity.this, MainActivity.class));
        notes.setOnClickListener(v -> redirectActivity(EventsActivity.this, NotesActivity.class));
        events.setOnClickListener(v -> recreate());
        settings.setOnClickListener(v -> redirectActivity(EventsActivity.this, SettingsActivity.class));
        about.setOnClickListener(v -> redirectActivity(EventsActivity.this, AboutActivity.class));


    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
}
