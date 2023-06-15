package com.example.jamz;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class EventDialogFragment extends DialogFragment {

    public static EventDialogFragment newInstance(String title, String description, int position) {
        EventDialogFragment fragment = new EventDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("description", description);
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnNewEventCreatedListener {
        void onNewEvent(String title, String description);
    }

    private OnNewEventCreatedListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_event, null);

        // Handle input fields and buttons
        EditText eventTitleInput = view.findViewById(R.id.event_title_input);
        EditText eventDescriptionInput = view.findViewById(R.id.event_description_input);
        Button cancelButton = view.findViewById(R.id.cancel_button);
        Button saveButton = view.findViewById(R.id.save_button);

        // Populate input fields when editing an event
        if (getArguments() != null) {
            eventTitleInput.setText(getArguments().getString("title", ""));
            eventDescriptionInput.setText(getArguments().getString("description", ""));
        }

        // Handle cancel button click
        cancelButton.setOnClickListener(v -> dismiss());

        // Handle save button click
        saveButton.setOnClickListener(v -> {
            String title = eventTitleInput.getText().toString().trim();
            String description = eventDescriptionInput.getText().toString().trim();
            if (!title.isEmpty() && !description.isEmpty()) {
                if (listener != null) {
                    listener.onNewEvent(title, description);
                }
            }
            dismiss();
        });

        builder.setView(view);

        return builder.create();
    }

    public void setOnNewEventCreatedListener(OnNewEventCreatedListener listener) {
        this.listener = listener;
    }
}