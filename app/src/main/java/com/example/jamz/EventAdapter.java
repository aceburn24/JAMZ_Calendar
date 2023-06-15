package com.example.jamz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> eventList;
    private EventInteractionListener eventInteractionListener;

    public EventAdapter(List<Event> eventList, EventInteractionListener eventInteractionListener) {
        this.eventList = eventList;
        this.eventInteractionListener = eventInteractionListener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_event, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public interface EventInteractionListener {
        void onEditEvent(int position);
        void onDeleteEvent(int position);
    }

    class EventViewHolder extends RecyclerView.ViewHolder {

        TextView eventTitle;
        TextView eventDescription;
        TextView eventDate;
        View eventItemContainer;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.eventTitle);
            eventDescription = itemView.findViewById(R.id.eventDescription);
            eventDate = itemView.findViewById(R.id.eventDate);
            eventItemContainer = itemView.findViewById(R.id.event_item_container);
            eventItemContainer.setOnClickListener(view -> showPopupMenu(view, getAdapterPosition()));
        }

        @SuppressLint("SimpleDateFormat")
        public void bind(Event event) {
            eventTitle.setText(event.getTitle());
            eventDescription.setText(event.getDescription());
            eventDate.setText(new SimpleDateFormat("yyyy/MM/dd").format(event.getDate()));
        }

        private void showPopupMenu(View view, int position) {
            Context context = view.getContext();
            PopupMenu popupMenu = new PopupMenu(context, view);
            popupMenu.inflate(R.menu.event_item_menu);
            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.action_edit_event) {
                    eventInteractionListener.onEditEvent(position);
                    return true;
                } else if (itemId == R.id.action_delete_event) {
                    eventInteractionListener.onDeleteEvent(position);
                    return true;
                } else {
                    return false;
                }
            });
            popupMenu.show();
        }
    }
}