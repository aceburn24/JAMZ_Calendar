package com.example.jamz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NotesActivity extends AppCompatActivity implements NotesAdapter.OnNoteClickListener {

    private static final int REQUEST_CODE_ADD_NOTE = 1001;
    private FloatingActionButton fabAddNote;
    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;
    private NoteViewModel noteViewModel;
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home;
    LinearLayout notes;
    LinearLayout about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        recyclerView = findViewById(R.id.recycler_view);
        int itemMargin = getResources().getDimensionPixelSize(R.dimen.item_margin);
        recyclerView.addItemDecoration(new ItemMarginDecoration(itemMargin));

        // Initialize the NotesAdapter and NoteViewModel
        notesAdapter = new NotesAdapter(this);
        recyclerView.setAdapter(notesAdapter);

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, notes -> {
            // Update the RecyclerView adapter with the new notes list.
            notesAdapter.setNotes(notes);
        });

        fabAddNote = findViewById(R.id.fab_add_note);
        fabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotesActivity.this, NoteDetailsActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_NOTE);
            }
        });

        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home = findViewById(R.id.home);
        notes = findViewById(R.id.notes);
        about = findViewById(R.id.about);

        menu.setOnClickListener(v -> openDrawer(drawerLayout));
        home.setOnClickListener(v -> redirectActivity(this, MainActivity.class));
        notes.setOnClickListener(v -> recreate());
        about.setOnClickListener(v -> redirectActivity(this, AboutActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK) {
            noteViewModel.getAllNotes();
        }
    }

    @Override
    public void onNoteClick(int position) {
        Note note = notesAdapter.getNoteAt(position);
        Intent intent = new Intent(this, NoteDetailsActivity.class);
        intent.putExtra(NoteDetailsActivity.EXTRA_NOTE_ID, note.getId());
        startActivityForResult(intent, REQUEST_CODE_ADD_NOTE);
    }

    private class ItemMarginDecoration extends RecyclerView.ItemDecoration {
        private final int itemMargin;

        public ItemMarginDecoration(int itemMargin) {
            this.itemMargin = itemMargin;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.top = itemMargin;
            outRect.bottom = itemMargin;
            outRect.left = itemMargin;
            outRect.right = itemMargin;
        }
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void redirectActivity(Activity activity, Class<?> secondActivity) {
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