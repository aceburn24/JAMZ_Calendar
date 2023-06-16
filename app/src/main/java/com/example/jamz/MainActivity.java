package com.example.jamz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements EventDialogFragment.OnNewEventCreatedListener, EventAdapter.EventInteractionListener {

    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home;
    LinearLayout notes;
    LinearLayout about;

    private MutableLiveData<Date> selectedDateLiveData;
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
        about = findViewById(R.id.about);

        menu.setOnClickListener(v -> openDrawer(drawerLayout));
        home.setOnClickListener(v -> recreate());
        notes.setOnClickListener(v -> redirectActivity(MainActivity.this, NotesActivity.class));
        about.setOnClickListener(v -> redirectActivity(MainActivity.this, AboutActivity.class));

        selectedDateLiveData = new MutableLiveData<>();
        selectedDateLiveData.setValue(new Date());

        Button datePickerButton = findViewById(R.id.datePickerButton);
        datePickerButton.setOnClickListener(v -> showDatePickerDialog());

        // Set up the RecyclerView
        setupRecyclerView();

        // Handle FloatingActionButton click
        FloatingActionButton addEventButton = findViewById(R.id.addEventButton);
        addEventButton.setOnClickListener(v -> {
            EventDialogFragment eventDialog = new EventDialogFragment();
            eventDialog.setOnNewEventCreatedListener(MainActivity.this);
            eventDialog.show(getSupportFragmentManager(), "addEvent");
        });
    }

    private void showDatePickerDialog() {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select Date");
        MaterialDatePicker<Long> picker = builder.build();

        picker.addOnPositiveButtonClickListener(selection -> {
            // Handle date selection
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.setTimeInMillis(selection);

            onDatePickerDateSelected(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));
        });

        picker.show(getSupportFragmentManager(), picker.toString());
    }

    private void setupRecyclerView() {
        RecyclerView eventsRecyclerView = findViewById(R.id.eventsRecyclerView);
        eventList = new ArrayList<>();

        eventAdapter = new EventAdapter(eventList, MainActivity.this);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventsRecyclerView.setAdapter(eventAdapter);

        filterEventsByDate();

        selectedDateLiveData.observe(this, date -> filterEventsByDate());
    }

    public void onDatePickerDateSelected(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        selectedDateLiveData.setValue(calendar.getTime());
    }

    private void filterEventsByDate() {
        Date date = selectedDateLiveData.getValue();
        if (date == null) return;

        // Populate your eventList with filtered events based on the date
        // Your actual implementation may vary depending on where your events are stored
    }

    private boolean isSameDay(Date date1, Date date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        return sdf.format(date1).equals(sdf.format(date2));
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