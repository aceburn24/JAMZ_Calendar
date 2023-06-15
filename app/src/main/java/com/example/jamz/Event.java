package com.example.jamz;

import java.util.Date;

public class Event {
    private String title;
    private String description;
    private Date date;

    public Event(String title, String description, Date date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }
}
