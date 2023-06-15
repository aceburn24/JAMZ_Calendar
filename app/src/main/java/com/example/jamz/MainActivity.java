package com.example.jamz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EventDialogFragment.OnNewEventCreatedListener, EventAdapter.EventInteractionListener {

    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home;
    LinearLayout notes;
    LinearLayout events;
    LinearLayout settings;
    LinearLayout about;

    private List<Event> eventList;
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home = findViewById(R.id.home);
        notes = findViewById(R.id.notes);
        events = findViewById(R.id.events);
        settings = findViewById(R.id.settings);
        about = findViewById(R.id.about);

        menu.setOnClickListener(v -> openDrawer(drawerLayout));
        home.setOnClickListener(v -> recreate());
        notes.setOnClickListener(v -> redirectActivity(MainActivity.this, NotesActivity.class));
        events.setOnClickListener(v -> redirectActivity(MainActivity.this, EventsActivity.class));
        settings.setOnClickListener(v -> redirectActivity(MainActivity.this, SettingsActivity.class));
        about.setOnClickListener(v -> redirectActivity(MainActivity.this, AboutActivity.class));

        // Set up the RecyclerView
        setupRecyclerView();

        // Handle FloatingActionButton click
        FloatingActionButton addEventButton = findViewById(R.id.addEventButton);
        addEventButton.setOnClickListener(v -> {
            EventDialogFragment eventDialog = new EventDialogFragment();
            eventDialog.setOnNewEventCreatedListener(MainActivity.this);
            eventDialog.show(getSupportFragmentManager(), "addEvent");
        });

        // Initialize and set up the MaterialDatePicker
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setValidator(DateValidatorPointForward.now());
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setCalendarConstraints(constraintsBuilder.build())
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.setTimeInMillis(selection);
            filterEventsByDate(selectedDate.getTime());
        });
        datePicker.show(getSupportFragmentManager(), datePicker.toString());
    }

    private void setupRecyclerView() {
        RecyclerView eventsRecyclerView = findViewById(R.id.eventsRecyclerView);
        eventList = new ArrayList<>();

        // Add some sample events
        eventList.add(new Event("Sample Event 1", "This is a sample event", new Date()));
        eventList.add(new Event("Sample Event 2", "This is another sample event", new Date()));

        eventAdapter = new EventAdapter(eventList, MainActivity.this);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventsRecyclerView.setAdapter(eventAdapter);
    }

    private void filterEventsByDate(Date date) {
        List<Event> filteredEvents = new ArrayList<>();

        for (Event event : eventList) {
            if (isSameDay(event.getDate(), date)) {
                filteredEvents.add(event);
            }
        }

        eventList.clear();
        eventList.addAll(filteredEvents);
        eventAdapter.notifyDataSetChanged();
    }

    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
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

    @Override
    public void onNewEvent(String title, String description) {
        Event newEvent = new Event(title, description, new Date());
        eventList.add(newEvent);
        eventAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEditEvent(int position) {
        Event event = eventList.get(position);
        EventDialogFragment eventDialog = EventDialogFragment.newInstance(event.getTitle(), event.getDescription(), position);
        eventDialog.setOnNewEventCreatedListener(MainActivity.this);
        eventDialog.show(getSupportFragmentManager(), "editEvent");
    }

    @Override
    public void onDeleteEvent(int position) {
        eventList.remove(position);
        eventAdapter.notifyItemRemoved(position);
    }
}