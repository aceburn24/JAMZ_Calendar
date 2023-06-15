package com.example.jamz;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class NoteDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_NOTE_ID = "com.example.jamz.EXTRA_NOTE_ID";
    private EditText titleEditText, contentEditText;
    private Note note;
    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        titleEditText = findViewById(R.id.titleEditText);
        contentEditText = findViewById(R.id.contentEditText);

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        if (getIntent().hasExtra(EXTRA_NOTE_ID)) {
            int noteId = Math.toIntExact(getIntent().getLongExtra(EXTRA_NOTE_ID, -1));
            if (noteId != -1) {
                noteViewModel.getNoteById(noteId).observe(this, loadedNote -> {
                    if (loadedNote != null) {
                        note = loadedNote;
                        titleEditText.setText(note.getTitle());
                        contentEditText.setText(note.getContent());
                    }
                });
            }
        }

        initView();
        initEditButton();

        ImageView saveImageView = findViewById(R.id.saveImageView);
        saveImageView.setOnClickListener(v -> {
            saveNote();
            finish();
        });

        Button deleteButton = findViewById(R.id.button_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNote();
            }
        });
    }

    @Override
    public void onBackPressed() {
        saveNote();
        super.onBackPressed();
    }

    private void initView() {
        titleEditText.setEnabled(false);
        contentEditText.setEnabled(false);
    }

    private void initEditButton() {
        Button buttonEdit = findViewById(R.id.button_edit);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleEditText.setEnabled(true);
                contentEditText.setEnabled(true);
            }
        });
    }

    private void saveNote() {
        String updatedTitle = titleEditText.getText().toString();
        String updatedContent = contentEditText.getText().toString();

        if (note != null) {
            note.setTitle(updatedTitle);
            note.setContent(updatedContent);
            noteViewModel.update(note);
        } else {
            // Handle the case when a new note is being added
            note = new Note(updatedTitle, updatedContent);
            noteViewModel.insert(note);
        }
    }

    public void deleteNote() {
        if (note != null) {
            noteViewModel.delete(note);
            Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "No note to delete", Toast.LENGTH_SHORT).show();
        }
    }
}